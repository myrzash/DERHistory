package kz.nis.history;

import java.util.ArrayList;

import kz.nis.history.adapter.DBAdapter;
import kz.nis.history.adapter.ListAdapter;
import kz.nis.history.extra.FontFactory;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class FragmentList extends ListFragment {
	private static final String ATTR_POS = "pos";
	//
	// private EditText[] edits = null;
	// = null;

	private int[] idsEdit = new int[] { R.id.editTextQuestion,
			R.id.editTextAnswer1, R.id.editTextAnswer2, R.id.editTextAnswer3,
			R.id.editTextAnswer4 };
	private int[] hints = new int[] { R.string.question,
			R.string.answerCorrect, R.string.answer2, R.string.answer3,
			R.string.answer4 };

	public static Fragment newInstance(Context context, int pos) {
		Bundle b = new Bundle();
		b.putInt(ATTR_POS, pos);
		return Fragment.instantiate(context, FragmentList.class.getName(), b);
	}

	private DBAdapter dbAdapter;
	private ListAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_list, container, false);

		final int POS = this.getArguments().getInt(ATTR_POS);
		Typeface typeface = FontFactory.getFont1(getActivity());

		Typeface bold = FontFactory.getBold(getActivity());
		dbAdapter = new DBAdapter(getActivity());
		try {
			dbAdapter.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// PARTID = POS + 1
		Cursor cursor = dbAdapter.getAllRecordsImportant(POS + 1);
		TextView textAttrActiv = (TextView) view
				.findViewById(R.id.textAttrCountActiv);
		textAttrActiv.setTypeface(typeface);
		textAttrActiv.setText(Main.getTranslate(getActivity(),
				R.string.selected));

		TextView textAttrAll = (TextView) view.findViewById(R.id.textAttrCount);
		textAttrAll.setTypeface(typeface);
		textAttrAll.setText(Main.getTranslate(getActivity(), R.string.all));
		TextView textActiv = (TextView) view.findViewById(R.id.textCountActiv);
		textActiv.setTypeface(bold);
		TextView textAll = (TextView) view.findViewById(R.id.textCount);
		textAll.setTypeface(bold);

		ArrayList<Integer> ids = new ArrayList<Integer>();
		ArrayList<String> quests = new ArrayList<String>();
		ArrayList<String> answers = new ArrayList<String>();
		ArrayList<Boolean> activs = new ArrayList<Boolean>();
		if (cursor.moveToFirst()) {
			do {

				ids.add(cursor.getInt(0));
				quests.add(cursor.getString(1));
				answers.add(cursor.getString(2));
				activs.add(Boolean.parseBoolean(cursor.getString(3)));

			} while (cursor.moveToNext());
		}

		adapter = new ListAdapter(getActivity(), dbAdapter, ids, quests,
				answers, activs, textActiv, textAll);
		setListAdapter(adapter);

		CheckBox checkBoxAll = (CheckBox) view.findViewById(R.id.checkBoxAll);
		checkBoxAll.setTag(POS);
		checkBoxAll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean activ = ((CheckBox) v).isChecked();
				// int partId = (Integer) v.getTag() + 1;
				adapter.changeActivies(activ);
			}
		});
		//
		ImageView imageAdd = (ImageView) view.findViewById(R.id.imageViewAdd);
		imageAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialogAdd(POS + 1);
				// startActivity(new Intent(ActivityEdit.class.getName()));
			}
		});
		return view;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {

		super.onListItemClick(l, v, position, id);
		int _id = adapter.getItem(position);

		Log.d(Main.LOG, "position=" + position + ", _id=" + _id);
		dialogUpdate(position, _id);
	}

	private void dialogAdd(final int partId) {
		final Dialog dialog = new Dialog(getActivity());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.editor);
		final EditText[] edits = new EditText[idsEdit.length];

		for (int i = 0; i < idsEdit.length; i++) {
			edits[i] = (EditText) dialog.findViewById(idsEdit[i]);
			edits[i].setTypeface(FontFactory.getFont1(getActivity()));
			edits[i].setHint(Main.getTranslate(getActivity(), hints[i]));
		}
		Button buttonOk = (Button) dialog.findViewById(R.id.buttonOk);
		buttonOk.setTypeface(FontFactory.getFont1(getActivity()));
		buttonOk.setText(Main.getTranslate(getActivity(), R.string.add));
		buttonOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.cancel();
				String[] values = new String[edits.length];
				for (int i = 0; i < edits.length; i++) {
					values[i] = edits[i].getText().toString();
				}
				int id = dbAdapter.insertRecord(values, partId);
				adapter.add(id, values[0], values[1], true);
				getListView().smoothScrollToPosition(getListView().getCount());
			}
		});

		MyTextWatcher textWatcher = new MyTextWatcher(buttonOk, edits);
		for (int i = 0; i < idsEdit.length; i++) {
			edits[i].addTextChangedListener(textWatcher);
		}
		Button buttonCancel = (Button) dialog.findViewById(R.id.buttonCancel);
		buttonCancel.setTypeface(FontFactory.getFont1(getActivity()));
		buttonCancel.setText(Main.getTranslate(getActivity(), R.string.cancel));
		buttonCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});
		dialog.getWindow().setBackgroundDrawableResource(
				android.R.drawable.alert_light_frame);
		dialog.show();
	}

	private void dialogUpdate(final int pos, final int id) {
		final Dialog dialog = new Dialog(getActivity());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.editor);

		final EditText[] edits = new EditText[idsEdit.length];
		Cursor cursor = dbAdapter.getRecord(id);
		if (cursor.moveToFirst()) {
			for (int i = 0; i < idsEdit.length; i++) {
				edits[i] = (EditText) dialog.findViewById(idsEdit[i]);
				edits[i].setTypeface(FontFactory.getFont1(getActivity()));
				edits[i].setHint(Main.getTranslate(getActivity(), hints[i]));
				edits[i].setText(cursor.getString(i));
			}
		}

		Button buttonOk = (Button) dialog.findViewById(R.id.buttonOk);
		buttonOk.setTypeface(FontFactory.getFont1(getActivity()));
		buttonOk.setText(Main.getTranslate(getActivity(), R.string.save));
		buttonOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.cancel();
				String[] values = new String[edits.length];
				for (int i = 0; i < edits.length; i++) {
					values[i] = edits[i].getText().toString();
				}
				dbAdapter.updateRecord(values, id);
				adapter.update(pos, values[0], values[1]);
			}
		});
		MyTextWatcher textWatcher = new MyTextWatcher(buttonOk, edits);
		for (int i = 0; i < idsEdit.length; i++) {
			edits[i].addTextChangedListener(textWatcher);
		}
		Button buttonCancel = (Button) dialog.findViewById(R.id.buttonCancel);
		buttonCancel.setTypeface(FontFactory.getFont1(getActivity()));
		buttonCancel.setText(Main.getTranslate(getActivity(), R.string.cancel));
		buttonCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});
		dialog.getWindow().setBackgroundDrawableResource(
				android.R.drawable.alert_light_frame);
		dialog.show();
	}

	@Override
	public void onStop() {
		if (dbAdapter != null)
			dbAdapter.close();
		super.onStop();
	}

	// private ArrayList<String> getArray(String attr) {
	// String[] mas = this.getArguments().getStringArray(attr);
	// ArrayList<String> array = new ArrayList<String>();
	// for (int i = 0; i < mas.length; i++)
	// array.add(mas[i]);
	// return array;
	// }
	//
	// private ArrayList<Boolean> getArray() {
	// boolean[] mas = this.getArguments().getBooleanArray(ATTR_ACTIV);
	// ArrayList<Boolean> array = new ArrayList<Boolean>();
	// for (int i = 0; i < mas.length; i++)
	// array.add(mas[i]);
	// return array;
	// }

	// private ArrayList<String> getDatas() {
	// // int pos = this.getArguments().getInt("pos");
	//
	// // ArrayList<Question> data = new ArrayList<Question>();
	// // for(int i=0;i<quests.length;i++)
	// // data.add(new Question(i+1, quests[i], answers[i],
	// // activs[i]));
	//
	//
	// // DBAdapter dbAdapter = new DBAdapter(getActivity());
	// // try {
	// // dbAdapter.open();
	// // } catch (Exception e) {
	// // e.printStackTrace();
	// // }
	// //
	// // Cursor cursor = dbAdapter.getNeedRecords(pos+1);
	// // if (cursor.moveToFirst()) {
	// // int i = 1;
	// // do {
	// //// Log.d("History","(size)="+cursor.getString(2));
	// //// quests[i] = cursor.getString(2);
	// // data.add(new Question(i, cursor.getString(1), cursor
	// // .getString(2),
	// // Boolean.parseBoolean(cursor.getString(3))));
	// // i++;
	// //
	// // } while (cursor.moveToNext());
	// // }
	// // dbAdapter.close();
	// return data;
	// }

}
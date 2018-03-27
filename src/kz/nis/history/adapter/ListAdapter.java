package kz.nis.history.adapter;

import java.util.ArrayList;

import kz.nis.history.Main;
import kz.nis.history.R;
import kz.nis.history.extra.FontFactory;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class ListAdapter extends BaseAdapter {
	private ArrayList<Integer> ids;
	private ArrayList<String> quests;
	private ArrayList<String> answers;
	private ArrayList<Boolean> activs;
	private DBAdapter dbAdapter;
	private TextView textActiv;
	private TextView textAll;
	private Context context;
	private Typeface typeface;
	private Typeface bold;

	public ListAdapter(Context context, DBAdapter dbAdapter,
			ArrayList<Integer> ids, ArrayList<String> quests,
			ArrayList<String> answers, ArrayList<Boolean> activs,
			TextView textActiv, TextView textAll) {
		this.dbAdapter = dbAdapter;
		this.ids = ids;
		this.quests = quests;
		this.answers = answers;
		this.activs = activs;
		this.textActiv = textActiv;
		this.textActiv.setText(getCountActiv());
		this.textAll = textAll;
		this.textAll.setText(getCount() + "");
		this.context = context;
		this.typeface = FontFactory.getFont1(context);
		this.bold = FontFactory.getBold(context);
	}

	@Override
	public int getCount() {
		return ids.size();
	}

	@Override
	public Integer getItem(int position) {
		return ids.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = convertView;
		if (view == null) {
			LayoutInflater lInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = lInflater.inflate(R.layout.item_list, parent, false);
			TextView textViewAttrAnswer = (TextView) view
					.findViewById(R.id.textViewAttrAnswer);
			textViewAttrAnswer.setTypeface(typeface);
			textViewAttrAnswer.setText(Main.getTranslate(this.context,
					R.string.answer));

		}
		TextView textItemNumber = (TextView) view
				.findViewById(R.id.textItemNumber);
		textItemNumber.setTypeface(typeface);
		textItemNumber.setText("" + (position + 1));

		TextView textItemQuest = (TextView) view
				.findViewById(R.id.textItemQuest);
		textItemQuest.setTypeface(typeface);
		textItemQuest.setText(quests.get(position));

		TextView textItemAnswer = (TextView) view
				.findViewById(R.id.textItemAnswer);
		textItemAnswer.setTypeface(bold);
		textItemAnswer.setText(answers.get(position));

		CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox1);
		checkBox.setOnClickListener(onClickCheckBox);
		checkBox.setTag(position + "/" + ids.get(position));
		checkBox.setChecked(activs.get(position));
		// ImageView delete = (ImageView) view.findViewById(R.id.deleteRecord);
		// delete.setOnClickListener(onClickListener);
		// delete.setTag(position);
		// if(position>10)
		// delete.setVisibility(position);
		return view;
	}

	OnClickListener onClickCheckBox = new OnClickListener() {

		@Override
		public void onClick(View v) {
			String tag = v.getTag().toString();
			int pos = getPosId(tag, 0);
			boolean activ = !activs.get(pos);
			activs.set(pos, activ);
			int id = getPosId(tag, 1);
			notifyDataSetChanged();
			dbAdapter.updateActiv(activ, id);
			textActiv.setText(getCountActiv());
		}

	};

	private CharSequence getCountActiv() {
		int k = 0;
		for (int i = 0; i < activs.size(); i++)
			if (activs.get(i) == true)
				k++;
		return "" + k;
	}

	private int getPosId(String tag, int where) {
		String[] array = tag.split("/");
		return Integer.parseInt(array[where]);
	}

	// OnClickListener onClickListener = new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	//
	// }
	// };
	// public void changeActivies(boolean key) {
	//
	// for (int i = 0; i < activs.length; i++)
	// activs[i]= key;
	// notifyDataSetChanged();
	// }
	public void changeActivies(boolean key) {
		for (int i = 0; i < activs.size(); i++) {
			activs.set(i, key);
			dbAdapter.updateActiv(key, ids.get(i));
		}
		notifyDataSetChanged();
		textActiv.setText(getCountActiv());
	}

	public void add(int id, String quest, String answer, boolean activ) {
		this.ids.add(id);
		this.quests.add(quest);
		this.answers.add(answer);
		this.activs.add(activ);
		notifyDataSetChanged();
		textActiv.setText(getCountActiv());
		textAll.setText(getCount() + "");
	}

	public void update(int pos, String quest, String answer) {
		this.quests.set(pos, quest);
		this.answers.set(pos, answer);
		this.activs.set(pos, true);
		notifyDataSetChanged();
		textActiv.setText(getCountActiv());
	}

}

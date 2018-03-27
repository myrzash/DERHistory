package kz.nis.history;
//package erc.historykz.ru;
//
//import java.util.ArrayList;
//
//import android.app.Activity;
//import android.app.Dialog;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.database.Cursor;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.BaseAdapter;
//import android.widget.CheckBox;
//import android.widget.CompoundButton;
//import android.widget.CompoundButton.OnCheckedChangeListener;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.SlidingDrawer;
//import android.widget.TextView;
//import android.widget.Toast;
//import android.widget.ViewFlipper;
//import erc.historykz.ru.adapter.DBAdapter;
//import erc.historykz.ru.adapter.Question;
//
//public class ActivityList extends Activity {
//	private int partID = 0;
//	ArrayList<Question> questions = new ArrayList<Question>();
//	public static ListAdapter adapter;
//	private ListView listView1;
//	public static DBAdapter dbAdapter;
//	private Cursor cursor;
//	// header list
//	private int countActiv;
//	private int count;
//	private TextView textCount;
//	private TextView textCountActiv;
//	private CheckBox checkBoxAll;
//	// flipper title
//	private ViewFlipper viewFlipperCategories;
//	private ImageView imageNext;
//	private ImageView imagePrev;
//	private final int ID_DELETE = 0;
//	private final int ID_SAVE = 1;
//	// private final int COUNT_NEED = 5;
//	private static EditText[] edits;
//	private SlidingDrawer slider;
//	private static int posItem;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.list);
//
////		openDB();
////		initViews();
////		if (edits == null)
////			edits = new EditText[] {
////					(EditText) findViewById(R.id.editTextQuestion),
////					(EditText) findViewById(R.id.editTextAnswer1),
////					(EditText) findViewById(R.id.editTextAnswer2),
////					(EditText) findViewById(R.id.editTextAnswer3),
////					(EditText) findViewById(R.id.editTextAnswer4) };
////		
////		fillData();
////		registerList();
////		registerCheckBoxAll();
//		
//	}
//
//	private void registerList() {
//		adapter = new ListAdapter(this, questions);
//		listView1.setAdapter(adapter);
//		listView1.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1,
//					int position, long arg3) {
//				posItem = position + 1;
//				slider.open();
//				Cursor c = dbAdapter.getRecord(posItem, partID);
//				for (int i = 0; i < edits.length; i++)
//					edits[i].setText(c.getString(i));
//			}
//		});
//	}
//
//	private void openDB() {
//		dbAdapter = new DBAdapter(this);
//		try {
//			dbAdapter.open();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	private void initViews() {
//		listView1 = (ListView) findViewById(R.id.listViewDB);
//		slider = (SlidingDrawer) findViewById(R.id.slider);
//		textCount = (TextView) findViewById(R.id.textCount);
//		textCountActiv = (TextView) findViewById(R.id.textCountActiv);
//		
//		viewFlipperCategories = (ViewFlipper) findViewById(R.id.viewFlipperCategories);
//		viewFlipperCategories.setOutAnimation(this, android.R.anim.fade_out);
//		viewFlipperCategories.setInAnimation(this, android.R.anim.fade_in);
//		viewFlipperCategories.setDisplayedChild(partID - 1);
//		imageNext = (ImageView) findViewById(R.id.ImageViewNext);
//		imagePrev = (ImageView) findViewById(R.id.imageViewPrev);
//		imageNext.setOnClickListener(new navigationFlip());
//		imagePrev.setOnClickListener(new navigationFlip());
//		if (partID == 1)
//			imagePrev.setVisibility(ImageView.INVISIBLE);
//	}
//
//	private class navigationFlip implements OnClickListener {
//
//		@Override
//		public void onClick(View v) {
//			if (countActiv >= ActivityPlay.QUANTITY_ANSWERS) {
//				switch (v.getId()) {
//				case R.id.ImageViewNext:
//					partID++;
//					if (partID == 1)
//						imagePrev.setVisibility(ImageView.VISIBLE);
//					if (partID == 4)
//						imageNext.setVisibility(ImageView.INVISIBLE);
//					viewFlipperCategories.showNext();
//					break;
//				case R.id.imageViewPrev:
//					partID--;
//					if (partID == 3)
//						imageNext.setVisibility(ImageView.VISIBLE);
//					if (partID == 0)
//						imagePrev.setVisibility(ImageView.INVISIBLE);
//					viewFlipperCategories.showPrevious();
//					break;
//
//				}
//				fillData();
//				clearEdits();
//			} else {
//				Toast.makeText(
//						ActivityList.this,
//						getString(R.string.toast_necessary)
//								+ (ActivityPlay.QUANTITY_ANSWERS - countActiv),
//						Toast.LENGTH_SHORT).show();
//			}
//		}
//
//	}
//
//	public void fillData() {
//		if(questions != null) questions.clear();
//		cursor = dbAdapter.getNeedRecords(partID);
//		count = cursor.getCount();
//		countActiv = dbAdapter.getActivRecords(partID).getCount();
//
//		textCount.setText("" + count);
//		textCountActiv.setText("" + countActiv);
//
//		int i = 1;
//		do {
//			questions.add(new Question(i, cursor.getString(1), cursor
//					.getString(2), Boolean.parseBoolean(cursor.getString(3))));
//			i++;
//
//		} while (cursor.moveToNext());
//
//		adapter.objects = questions;
//		adapter.notifyDataSetChanged();
//	}
//
//	private void registerCheckBoxAll() {
//		checkBoxAll = (CheckBox) findViewById(R.id.checkBoxAll);
//		checkBoxAll.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView,
//					boolean isChecked) {
//								
//			}
//		});
//	}
//	
////	private class Task extends AsyncTask<Boolean, Void, Void>{
////
////		@Override
////		protected void onPreExecute() {
////			progressDialog.show();
////			super.onPreExecute();
////		}
////		
////		@Override
////		protected Void doInBackground(Boolean... key) {
////			for (int i = 1; i <= dbAdapter.getNeedRecords(partID)
////					.getCount(); i++){
////				dbAdapter.updateActiv(key[0], partID, i);
////			}
////			if(questions != null) questions.clear();
////			cursor = dbAdapter.getNeedRecords(partID);
////			count = cursor.getCount();
////			countActiv = dbAdapter.getActivRecords(partID).getCount();
////
////			int i = 1;
////			do {
////				questions.add(new Question(i, cursor.getString(1), cursor
////						.getString(2), Boolean.parseBoolean(cursor.getString(3))));
////				i++;
////
////			} while (cursor.moveToNext());
////			
////			return null;
////		}
////		@Override
////		protected void onPostExecute(Void result) {
////			
////			textCount.setText("" + count);
////			textCountActiv.setText("" + countActiv);
////			adapter.objects = questions;
////			adapter.notifyDataSetChanged();
////			if(progressDialog.isShowing()) progressDialog.dismiss();
////			
////			super.onPostExecute(result);
////		}
////		
////		
////	}
//
//	private void dialog(final int position, final int action) {
//		final Dialog dialog = new Dialog(ActivityList.this);
//		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//		dialog.setContentView(R.layout.dialog);
//
//		dialog.getWindow().setBackgroundDrawableResource(
//				android.R.drawable.picture_frame);
//		((TextView) dialog.findViewById(R.id.textMessageDialog))
//				.setText((action == ID_SAVE) ? R.string.save_changes
//						: R.string.delete);
//		((ImageView) dialog.findViewById(R.id.imageViewYes))
//				.setOnClickListener(new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						if (action == ID_DELETE) {
//							dbAdapter.deleteRecord(partID, position);
//							Toast.makeText(ActivityList.this, R.string.deleted,
//									Toast.LENGTH_SHORT).show();
//						}
//						if (action == ID_SAVE) {
//							String str[] = new String[edits.length];
//							for (int i = 0; i < edits.length; i++) {
//								str[i] = edits[i].getText().toString();
//							}
////							if (keyUpdate)
////								dbAdapter.updateRecord(str, partID, posItem);
////							else
////								dbAdapter.insertRecord(str, partID);
//
//							clearEdits();
//							Toast.makeText(ActivityList.this, R.string.saved,
//									Toast.LENGTH_SHORT).show();
//						}
//
//						fillData();
//						dialog.cancel();
//					}
//				});
//		((ImageView) dialog.findViewById(R.id.imageViewNo))
//				.setOnClickListener(new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						dialog.cancel();
//					}
//				});
//
//		dialog.show();
//	}
//
//	public void onClickSave(View v) {
//		if (checkEdits()) {
//			dialog(posItem, ID_SAVE);
//		} else {
//			Toast.makeText(ActivityList.this, R.string.fill_all_fields,
//					Toast.LENGTH_SHORT).show();
//		}
//
//	}
//
////	public void onClickCancel(View v) {
////		if (keyUpdate) {
////			Cursor c = dbAdapter.getRecord(posItem, partID);
////			for (int i = 0; i < edits.length; i++)
////				edits[i].setText(c.getString(i));
////		} else
////			clearEdits();
////	}
//
//	public void onClickClear(View v) {
//		int pos = 0;
//		switch (v.getId()) {
//		case R.id.imageViewClear1:
//			pos = 0;
//			break;
//		case R.id.imageViewClear2:
//			pos = 1;
//			break;
//		case R.id.imageViewClear3:
//			pos = 2;
//			break;
//		case R.id.imageViewClear4:
//			pos = 3;
//			break;
//		case R.id.imageViewClear5:
//			pos = 4;
//			break;
//		}
//		edits[pos].setText("");
//	}
//
//	@Override
//	public void onBackPressed() {
//		if (ActivityPlay.QUANTITY_ANSWERS <= countActiv) {
//			super.onBackPressed();
//			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//		} else
//			Toast.makeText(
//					ActivityList.this,
//					getString(R.string.toast_necessary)
//							+ (ActivityPlay.QUANTITY_ANSWERS - countActiv),
//					Toast.LENGTH_SHORT).show();
//
//	}
//
//	public void onClickHome(View v) {
//		onBackPressed();
//	}
//
//	public void onClickAdd(View v) {
//		slider.open();
//		clearEdits();
//	}
//
//	// dopolnitelno
//	private void clearEdits() {
//		for (int i = 0; i < edits.length; i++)
//			edits[i].setText("");
//	}
//
//	private boolean checkEdits() {
//		for (int i = 0; i < edits.length; i++) {
//			if (TextUtils.isEmpty(edits[i].getText().toString()))
//				return false;
//		}
//		return true;
//	}
//
//	// ------------------------------------------------------------------------------------------------------------------
//	// ------------------------------------------------------------------------------------------------------------------
//	// ListAdapter
//	
//}

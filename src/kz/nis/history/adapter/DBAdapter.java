package kz.nis.history.adapter;

import java.util.ArrayList;

import kz.nis.history.Main;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBAdapter {

	private final Context context;
	private String TABLE_NAME;
	private static final String[] TABLE_NAMES = new String[]{"historykz","historyru","historyen"};
	private static final String TABLE_USER = "users";
	private static final String ATTR_ID = "_id";

	private static final String ATTR_QUESTION = "quest";
	private static final String ATTR_ANSWER_1 = "answer1";
	private static final String ATTR_ANSWER_2 = "answer2";
	private static final String ATTR_ANSWER_3 = "answer3";
	private static final String ATTR_ANSWER_4 = "answer4";
	private static final String ATTR_PART_INDEX = "partindex";
	private static final String ATTR_ACTIV = "activ";
	// TABLE USERS
	private static final String ATTR_USER_NAME = "username";
	private static final String ATTR_USER_PSWD = "pswd";
	private static final String ATTR_KEY_LOGIN = "login";
	private static final String ATTR_TODAY = "date";
	private static final String ATTR_SCORE = "score";
	// private static final String ATTR_LEVEL = "level";
	// private static final String[] ATTR_SCORES = new String[] { "score1",
	// "score2", "score3", "score4", "score5" };

	private static String[] ATTRS_5 = new String[] { ATTR_QUESTION,
			ATTR_ANSWER_1, ATTR_ANSWER_2, ATTR_ANSWER_3, ATTR_ANSWER_4 };

	private DBOpenHelper dbHelper;
	private SQLiteDatabase DB;
	private ContentValues cv;

	public DBAdapter(Context ctx) {
		this.context = ctx;
		this.TABLE_NAME = 	TABLE_NAMES[Main.LANG];
		try {
			this.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void open() throws Exception {
		dbHelper = new DBOpenHelper(context);
		DB = dbHelper.openDataBase();
		Log.d(Main.LOG, "openDataBase");
	}

	public void close() {
		Log.d(Main.LOG, "close");
		if (dbHelper != null)
			dbHelper.close();
	}

	public Cursor getAllRecords() {
		Log.d(Main.LOG, "getAllRecords");
		Cursor cursor = DB.query(TABLE_NAME, null, null, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();
		return cursor;
	}

//	public int getCountDatas(int partID) {
//		return this.getAllRecords(partID).getCount();
//	}
	public Cursor getAllRecords(int partID) {
		Log.w(Main.LOG, "getAllRecords partID");
		String selection = ATTR_PART_INDEX + " = " + partID;
		Cursor cursor = DB.query(TABLE_NAME, null, selection, null, null, null,
				null);
		if (cursor != null)
			cursor.moveToFirst();
		return cursor;
	}
	public Cursor getAllRecordsImportant(int partID) {
		Log.w(Main.LOG, "getAllRecordsImportant partID");
		String selection = ATTR_PART_INDEX + " = " + partID;
		Cursor cursor = DB.query(TABLE_NAME, new String[] { ATTR_ID,
				ATTR_QUESTION, ATTR_ANSWER_1, ATTR_ACTIV }, selection, null,
				null, null, null);
		if (cursor != null)
			cursor.moveToFirst();
		return cursor;
	}

	public int[] getActivIds(int partId) {
		// String selection = ATTR_PART_INDEX + " = " + partId + " AND "
		// + ATTR_ACTIV + "='true' ";
		String selection = String.format("%s=%d AND %s='true'",
				ATTR_PART_INDEX, partId, ATTR_ACTIV);
		Cursor cursor = DB.query(TABLE_NAME, new String[] { ATTR_ID },
				selection, null, null, null, null);

		Log.w(Main.LOG, "getActivIds cursor.getCount():" + cursor.getCount());
		if (cursor.moveToFirst()) {
			int[] arr = new int[cursor.getCount()];
			int i = 0;
			do {
				arr[i++] = cursor.getInt(0);
			} while (cursor.moveToNext());
			return arr;
		} else
			return null;
	}

	public Cursor getRecord(int id) {
		Cursor cursor = DB.query(true, TABLE_NAME, ATTRS_5, ATTR_ID + "=" + id,
				null, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();
		return cursor;
	}

	public void updateRecord(String[] str, int id) {
		cv = new ContentValues();
		for (int i = 0; i < str.length; i++) {
			cv.put(ATTRS_5[i], str[i]);
		}
		cv.put(ATTR_ACTIV, "true");
		String whereClause = ATTR_ID + " = " + id;
		DB.update(TABLE_NAME, cv, whereClause, null);
	}

	public void updateActiv(boolean key, int id) {
		cv = new ContentValues();
		cv.put(ATTR_ACTIV, String.valueOf(key));
		DB.update(TABLE_NAME, cv, ATTR_ID + " = " + id, null);
	}

	public int insertRecord(String[] str, int partID) {
		cv = new ContentValues();
		for (int i = 0; i < str.length; i++) {
			cv.put(ATTRS_5[i], str[i]);
		}
		cv.put(ATTR_PART_INDEX, partID);
		// cv.put(ATTR_NUMBER_ANSWER, getAllRecords(partID).getCount()+1);
		DB.insert(TABLE_NAME, null, cv);

		Cursor cursor = DB.query(TABLE_NAME, new String[] { ATTR_ID }, null,
				null, null, null, null, null);
		if (cursor != null)
			cursor.moveToLast();
		int id = cursor.getInt(0);
		return id;
	}

	public void deleteRecord(int id) {
		Log.d(Main.LOG, "Delete Record: id:" + id);
		DB.delete(TABLE_NAME, ATTR_ID + " = " + id, null);
	}

	//
	//
	//
	//
	public String getLastUserName() {
		Cursor cursor = DB.query(TABLE_USER, new String[] { ATTR_USER_NAME },
				ATTR_KEY_LOGIN + "='true'", null, null, null, null);
		if (cursor.moveToFirst())
			return cursor.getString(0);
		else
			return null;
	}

	public ArrayList<String> getUserNames() {
		Cursor cursor = DB.query(TABLE_USER, new String[] { ATTR_USER_NAME },
				null, null, null, null, null);
		if (cursor.moveToFirst()) {
			ArrayList<String> names = new ArrayList<String>();
			do {
				names.add(cursor.getString(0));
			} while (cursor.moveToNext());
			return names;
		} else
			return null;
	}

	public boolean existUser(String name) {
		name = "'" + name + "'";
		Cursor cursor = DB.query(TABLE_USER, new String[] { ATTR_USER_NAME },
				ATTR_USER_NAME + "=" + name, null, null, null, null);
		if (cursor.moveToFirst()) {
			Log.w(Main.LOG, "name: " + name + " exists");
			return true;
		}
		return false;
	}

	public CharSequence getPassword(String name) {
		name = "'" + name + "'";
		Cursor cursor = DB.query(TABLE_USER, new String[] { ATTR_USER_PSWD },
				ATTR_USER_NAME + "=" + name, null, null, null, null);
		if (cursor.moveToFirst())
			return cursor.getString(0);
		return null;
	}

	public void updateLogin(String name, boolean login) {
		name = "'" + name + "'";
		cv = new ContentValues();
		cv.put(ATTR_KEY_LOGIN, String.valueOf(login));
		DB.update(TABLE_USER, cv, ATTR_USER_NAME + "=" + name, null);
		Log.w(Main.LOG, "name=" + name + ", login=" + login);
	}

	public void insertUser(String name, String pswd) {
		if (existUser(name)) {
			return;
		}
		cv = new ContentValues();
		cv.put(ATTR_USER_NAME, name);
		cv.put(ATTR_USER_PSWD, pswd);
		DB.insert(TABLE_USER, null, cv);
		Log.d(Main.LOG, "--- insertUser ---");
	}

	public String[] getAttrsUser() {
		return new String[] { ATTR_ID, ATTR_USER_NAME,
				DBAdapter.ATTR_SCORE };
	}

	public Cursor getAllUsers() {
		Cursor cursor = DB.query(TABLE_USER, getAttrsUser(), null, null, null,
				null, ATTR_TODAY);
		if (cursor != null)
			cursor.moveToFirst();
		return cursor;
	}
	
	public void updateScore(String name, double score) {
		score = Math.rint(100*score)/100;
//		String query = String.format(" UPDATE %s set WHERE %s='%s' AND %s<%.2f ",TABLE_USER,ATTR_USER_NAME,name,ATTR_SCORE,score);
		
		String query = " UPDATE "+TABLE_USER +" SET "+ATTR_SCORE+"="+score+" WHERE "+ATTR_USER_NAME+"='"+name+"' AND "+ATTR_SCORE+"<"+score;
		DB.execSQL(query);
//		cv = new ContentValues();
//		cv.put(ATTR_SCORE, score);
//		DB.update(TABLE_USER, cv, query, null);
	}	
//	private void locationDatabase() {
//		String ATTR_PART_INDEX = "partindex";
//		 String[] ATTRS_5 = new String[] { "quest","answer1","answer2","answer3","answer4"};
//		 DBAdapter dbAdapter = new DBAdapter(getApplicationContext());		
//		DBAdapterOther dbAdapterKZ = new DBAdapterOther(getApplicationContext());
//		Cursor cursor = dbAdapterKZ.getAllRecords();
//		if(cursor.moveToFirst()){
//			do{
//				Log.w(Main.LOG,""+cursor.getInt(cursor.getColumnIndex(ATTR_PART_INDEX)));
//				String[] values =new String[ATTRS_5.length];
//				int i=0;
//				for(String attr:ATTRS_5){
//					values[i++]=cursor.getString(cursor.getColumnIndex(attr));
//				}
//				dbAdapter.insertRecord(values, cursor.getInt(cursor.getColumnIndex(ATTR_PART_INDEX)));
//			}while(cursor.moveToNext());
//		}else {
//			Log.w(Main.LOG,"null");
//		}		
//	}
}

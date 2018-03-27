package kz.nis.history;

import kz.nis.history.adapter.DBAdapter;
import kz.nis.history.extra.FontFactory;
import android.app.Activity;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityUsers extends Activity {
	private ListView listViewUsers;
	private DBAdapter dbAdapter;
	private UserAdapter scAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_users);
		int[] ids = new int[]{R.id.textViewTitle,R.id.textViewUserName,R.id.textViewScore};
		int[] texts = new int[]{R.string.users, R.string.user_name, R.string.best_result};
		Typeface typeface = FontFactory.getFont1(getApplicationContext());
		int i=0;
		for(int id : ids){
		TextView text = (TextView) findViewById(id);
		text.setText(Main.getTranslate(getApplicationContext(), texts[i++]));
		text.setTypeface(typeface);
		}
		listViewUsers = (ListView) findViewById(R.id.listViewUsers);
		dbAdapter = new DBAdapter(this);
		Cursor cursor = dbAdapter.getAllUsers();
		if (cursor.getCount() == 0) {
			Toast.makeText(ActivityUsers.this, R.string.user_list_is_empty,
					Toast.LENGTH_SHORT).show();
			onBackPressed();
			finish();
		} else
			populateList(cursor);
	}

	// private Cursor getUserDatas() {
	// dbAdapter = new DBAdapter(this);
	// Cursor cursor = null;
	// try {
	// dbAdapter.open();
	// cursor = dbAdapter.getDataUsers();
	// dbAdapter.close();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return cursor;
	// }

	private void populateList(Cursor cursor) {
		startManagingCursor(cursor);
		String[] from = dbAdapter.getAttrsUser();
		int[] to = new int[] { R.id.textItemUserID, R.id.textItemUserName,R.id.textItemLevel };
		scAdapter = new UserAdapter(this, R.layout.item_users, cursor,
				from, to);
		listViewUsers.setAdapter(scAdapter);
	}

	public void onClickHome(View v) {
		onBackPressed();
		overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out);
	}

	@Override
	protected void onStop() {
		if (dbAdapter != null)
			dbAdapter.close();
		super.onStop();
	}
}

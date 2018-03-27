package kz.nis.history;

import kz.nis.history.adapter.DBAdapter;
import kz.nis.history.cover.CirclePageIndicator;
import kz.nis.history.cover.CoverPageAdapter;
import kz.nis.history.cover.CoverPageTransformer;
import kz.nis.history.extra.FontFactory;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityInput extends FragmentActivity {

	// private static final int REQUEST_CODE_LANG = 1;
	private ViewPager pager;
	private SharedPreferences sharedPreferences;
	private static long back_pressed;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_input);
		if (getIntent().getAction() == "changelang") {
			Main.LANG = getIntent().getIntExtra("lang", 0);
			savePreferences(Main.LANG);
		} else
			Main.LANG = getLang();

		if (isLastUser()) {
			finish();
			Intent i = new Intent(ActivityInput.this, Main.class);
			startActivity(i);
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
		} else {
			Animation animRepeat3 = AnimationUtils.loadAnimation(getApplicationContext(),
					R.anim.anim_alpha_repeat_2);
			TextView text = (TextView) findViewById(R.id.textViewAppName);
			text.setTypeface(FontFactory.getFont1(getApplicationContext()));
			text.startAnimation(animRepeat3);
			registerPager();
			if (getIntent().getAction() == "changelang") {
				pager.setCurrentItem(1, false);
			}

		}
	}

	private boolean isLastUser() {
		DBAdapter dbAdapter = new DBAdapter(ActivityInput.this);
		String user = dbAdapter.getLastUserName();
		dbAdapter.close();
		if (user != null)
			return true;
		else
			return false;
	}

	private void registerPager() {
		Log.d("LOGS", "registerPager");

		pager = (ViewPager) findViewById(R.id.coverViewPager);
		CoverPageAdapter coverPageAdapter = new CoverPageAdapter(this,
				this.getSupportFragmentManager());
		pager.setPageTransformer(false, new CoverPageTransformer());
		pager.setAdapter(coverPageAdapter);
		CirclePageIndicator mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
		mIndicator.setViewPager(pager);

	}

	private int getLang() {
		sharedPreferences = getPreferences(MODE_PRIVATE);
		return sharedPreferences.getInt("lang", 0);
	}

	private void savePreferences(int lang) {
		sharedPreferences = getPreferences(MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt("lang", lang);
		editor.commit();
	}

	@Override
	public void onBackPressed() {
		if (back_pressed + 2000 > System.currentTimeMillis()) {
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
		} else
			Toast.makeText(getApplicationContext(),
					Main.getTranslate(ActivityInput.this, R.string.toast_exit),
					Toast.LENGTH_SHORT).show();
		back_pressed = System.currentTimeMillis();
	}
}

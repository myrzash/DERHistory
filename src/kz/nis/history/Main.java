package kz.nis.history;

import java.text.MessageFormat;

import kz.nis.history.adapter.DBAdapter;
import kz.nis.history.extra.FontFactory;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class Main extends Activity implements OnClickListener {

	public static final String LOG = "History";
	public static int LANG = 0;
	private int part;
	private ImageView imageCategory;

	private DBAdapter dbAdapter;
	public static String USER_NAME;
	private TextView textTitle;
	public static final String INTENT_PART = "part";
	public static final String INTENT_SCORE = "score";
	public static final String INTENT_RESTART = "restart";
	private static final int[] ids = new int[] { R.id.btn1, R.id.btn2,
			R.id.btn3, R.id.btn4, R.id.btn5 };
	private String[] titles;
	private Animation animRepeat3;
	private static final int[] drawables = new int[] {
			R.drawable.category_drevnij, R.drawable.category_srednevekovie,
			R.drawable.category_khanstvo, R.drawable.category_xx_vek,
			R.drawable.category_nezavisimy };
	private static final int[][] raws = new int[][] {
			{ R.raw.kaz2, R.raw.kaz3, R.raw.kaz4, R.raw.kaz5, R.raw.kaz6 },
			{ R.raw.ru2, R.raw.ru3, R.raw.ru4, R.raw.ru5, R.raw.ru6 },
			{ R.raw.english2, R.raw.english3, R.raw.english4, R.raw.english5,
					R.raw.english6 } };
	private static final int[] select_sounds = new int[] { R.raw.kaz1,
			R.raw.ru1, R.raw.english1 };
	private MediaPlayer mediaPlayer = null;
	private Animation animSelect;
	private static MediaPlayer mediaPlayerMusic = null;
	private CheckBox soundView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		part = 0;

		dbAdapter = new DBAdapter(Main.this);
		USER_NAME = dbAdapter.getLastUserName();

		mediaPlayer = MediaPlayer.create(this, select_sounds[LANG]);
		mediaPlayer.start();

		if (mediaPlayerMusic == null) {
			mediaPlayerMusic = MediaPlayer.create(this, R.raw.music_history);
			mediaPlayerMusic.setVolume(0.4f, 0.4f);
			mediaPlayerMusic.setLooping(true);
			mediaPlayerMusic.start();
		}
		
		
		soundView = (CheckBox) findViewById(R.id.checkBoxSound);
		soundView.setChecked(mediaPlayerMusic.isPlaying());
		soundView.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (mediaPlayer == null)
					return;
				if (isChecked) {
					if (!mediaPlayerMusic.isPlaying()) {
						mediaPlayerMusic.start();
					}
				} else {
					if (mediaPlayerMusic.isPlaying()) {
						mediaPlayerMusic.pause();
					}
				}
			}
		});
//		soundView.startAnimation(animRepeat3);
		// if (mediaPlayer == null) {
		// mediaPlayer = MediaPlayer.create(this, R.raw.symphony);
		// mediaPlayer.setLooping(true);
		// mediaPlayer.start();
		// findViewById(R.id.tableRLayoutExercises).startAnimation(
		// AnimationUtils.loadAnimation(getApplicationContext(),
		// R.anim.from_bottom));
		//
		// }
		// soundView = (CheckBox) findViewById(R.id.checkBoxSound);
		// soundView.setChecked(mediaPlayer.isPlaying());
		// soundView.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		//
		// @Override
		// public void onCheckedChanged(CompoundButton buttonView,
		// boolean isChecked) {
		// if (mediaPlayer == null)
		// return;
		// if (isChecked) {
		// if (!mediaPlayer.isPlaying()) {
		// mediaPlayer.start();
		// }
		// } else {
		// if (mediaPlayer.isPlaying()) {
		// mediaPlayer.pause();
		// }
		// }
		// }
		// });
		imageCategory = (ImageView) findViewById(R.id.imageCategories);
		textTitle = (TextView) findViewById(R.id.textTitle);
		textTitle.setTypeface(FontFactory.getFont1(getApplicationContext()));
		textTitle.setText(getTranslate(this, R.string.select_category));

		animRepeat3 = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.anim_alpha_repeat_3);
		textTitle.startAnimation(animRepeat3);
		titles = getResources().getStringArray(R.array.Categories);

		for (int i = 0; i < ids.length; i++) {
			Button button = (Button) findViewById(ids[i]);
			button.setTypeface(FontFactory.getFont1(getApplicationContext()));
			button.setOnClickListener(this);
			button.setText(MessageFormat.format(titles[i], LANG));
		}
		ImageView imageEdit = (ImageView) findViewById(R.id.imageEdit);
		imageEdit.setOnClickListener(this);
		imageEdit.startAnimation(animRepeat3);
		ImageView imageScore = (ImageView) findViewById(R.id.imageScore);
		imageScore.setOnClickListener(this);
		imageScore.startAnimation(animRepeat3);
		ImageView imageLogOut = (ImageView) findViewById(R.id.imageLogOut);
		imageLogOut.setOnClickListener(this);
		Button btnStart = (Button) findViewById(R.id.btnStart);
		btnStart.setOnClickListener(this);
		animSelect = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.anim_effect_select);

	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if(mediaPlayerMusic!=null)
		mediaPlayerMusic.setVolume(0.2f, 0.2f);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(mediaPlayerMusic!=null)
		mediaPlayerMusic.setVolume(0.4f, 0.4f);
	}

	private void changeCategory(int tag) {
		this.part = tag + 1;
		textTitle.setText(MessageFormat.format(titles[tag], LANG));
		imageCategory.setImageResource(drawables[tag]);
		if (mediaPlayer.isPlaying())
			mediaPlayer.stop();
		mediaPlayer = MediaPlayer.create(this, raws[LANG][tag]);
		mediaPlayer.start();
	}

	private void categoryAnimation() {
		textTitle.setText(getTranslate(getApplicationContext(),
				R.string.select_category));
		textTitle.startAnimation(AnimationUtils.loadAnimation(this,
				R.anim.select_category));
		if (mediaPlayer.isPlaying())
			mediaPlayer.stop();
		mediaPlayer = MediaPlayer.create(this, select_sounds[LANG]);
		mediaPlayer.start();
	}

	private static long back_pressed;

	@Override
	public void onBackPressed() {
		if (back_pressed + 2000 > System.currentTimeMillis()) {
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		} else
			Toast.makeText(getBaseContext(),
					getTranslate(getApplicationContext(), R.string.toast_exit),
					Toast.LENGTH_SHORT).show();
		back_pressed = System.currentTimeMillis();
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.btn1:
			changeCategory(0);
			break;
		case R.id.btn2:
			changeCategory(1);
			break;
		case R.id.btn3:
			changeCategory(2);
			break;
		case R.id.btn4:
			changeCategory(3);
			break;
		case R.id.btn5:
			changeCategory(4);
			break;
		case R.id.btnStart:
			if (part == 0) {
				categoryAnimation();
			} else {
				int need = ActivityPlay.quantity - getCountData(part);
				if (need > 0) {
					String msg = getTranslate(getApplicationContext(),
							R.string.toast_necessary) + ":" + need;
					Toast.makeText(getApplicationContext(), msg,
							Toast.LENGTH_SHORT).show();
				} else {
					Intent i = new Intent(Main.this, ActivityPlay.class);
					i.putExtra("part", this.part);
					i.setAction("fromMain");
					ActivitySplitAnimationUtil.startActivity(Main.this, i);
				}
			}
			break;
		case R.id.imageEdit:
			intent = new Intent(Main.this, ActivityList.class);
			break;
		case R.id.imageScore:
			intent = new Intent(Main.this, ActivityUsers.class);
			break;
		case R.id.imageLogOut:
			dbAdapter.updateLogin(USER_NAME, false);
			USER_NAME = null;
			finish();
			startActivity(new Intent(Main.this, ActivityInput.class));
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
			break;
		}
		if (intent != null) {
			startActivity(intent);
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
		} else {
			v.startAnimation(animSelect);
		}
	}

	private int getCountData(int partID) {
		DBAdapter dbAdapter = new DBAdapter(Main.this);
		try {
			dbAdapter.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
		int[] arr = dbAdapter.getActivIds(partID);
		dbAdapter.close();
		if (arr == null)
			return 0;
		else
			return arr.length;
	}

	@Override
	protected void onDestroy() {
		if (dbAdapter != null)
			dbAdapter.close();
		if (animRepeat3 != null)
			animRepeat3.cancel();
		if (mediaPlayerMusic.isPlaying())
			mediaPlayerMusic.stop();
		mediaPlayerMusic.release();
		mediaPlayerMusic = null;
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data == null)
			return;
		int part = data.getIntExtra(INTENT_PART, 0);
		Log.w(Main.LOG, "part" + part);
		double score = data.getDoubleExtra(INTENT_SCORE, 0);
		Log.w(Main.LOG, "score" + score);
		dbAdapter.updateScore(USER_NAME, score);

		boolean restart = data.getBooleanExtra(INTENT_RESTART, false);
		Log.w(Main.LOG, "restart=" + restart);
		if (restart) {
			startActivityForResult(
					new Intent(Main.this, ActivityPlay.class).putExtra(
							INTENT_PART, part), 1);
			overridePendingTransition(0, 0);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public static String getTranslate(Context context, int resId) {
		return MessageFormat.format(context.getString(resId), LANG);
	}
}
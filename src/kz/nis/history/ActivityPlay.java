package kz.nis.history;

import java.text.MessageFormat;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import kz.nis.history.adapter.DBAdapter;
import kz.nis.history.extra.FontFactory;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.GetChars;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ActivityPlay extends Activity implements OnClickListener {

	private DBAdapter dbAdapter;
	public static final int quantity = 15;
	private int part;
	private int[] ids = null;
	private int position = 0;
	private TextView textQuestion;
	private short trueAnswerID = 0;
	private Button[] buttons;
	private Timer time;
	private Animation animButton;
	private ProgressBar progressBar;
	private int positive = 0;
	private ImageView imageHorse;
	private TextView[] textLevels;
	private Animation animRepeat;
	private Animation animRepeat2;
	private Animation animRepeat3;
	private MediaPlayer mediaPlayer = null;
	private TextView textTimer;
	private int BROWN;
	private int TRANSPARENT_WHITE;
	private int WHITE;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getIntent().getAction() == "fromMain") {
			ActivitySplitAnimationUtil.prepareAnimation(this);
			setContentView(R.layout.play);
			ActivitySplitAnimationUtil.animate(this, 1200);
		} else
			setContentView(R.layout.play);
		BROWN  = getResources().getColor(R.color.text_color);
		TRANSPARENT_WHITE  = getResources().getColor(R.color.transparent_light);
		WHITE  = getResources().getColor(android.R.color.white);


		openDB();
		part = getIntent().getIntExtra("part", 0);
		Typeface typeface = FontFactory.getFont1(getApplicationContext());
		textQuestion = (TextView) findViewById(R.id.textViewQuestion);
		textQuestion.setTypeface(typeface);
		TextView textTitle = (TextView) findViewById(R.id.textViewCategoryName);
		textTitle.setTypeface(typeface);
		String[] categories = getResources().getStringArray(R.array.Categories);
		String title = MessageFormat.format(categories[part - 1], Main.LANG);
		textTitle.setText(title);
		progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		buttons = new Button[] { (Button) findViewById(R.id.buttonVariant1),
				(Button) findViewById(R.id.buttonVariant2),
				(Button) findViewById(R.id.buttonVariant3),
				(Button) findViewById(R.id.buttonVariant4) };
		imageHorse = (ImageView) findViewById(R.id.imageHorse);
		textLevels = new TextView[] { (TextView) findViewById(R.id.textLevel1),
				(TextView) findViewById(R.id.textLevel2),
				(TextView) findViewById(R.id.textLevel3),
				(TextView) findViewById(R.id.textLevel4),
				(TextView) findViewById(R.id.textLevel5) };

		String[] levels = getResources().getStringArray(R.array.Levels);
		for (int i = 0; i < textLevels.length; i++) {
			textLevels[i].setTypeface(typeface);
			textLevels[i].setText(MessageFormat.format(levels[i], Main.LANG));
		}

		for (int i = 0; i < buttons.length; i++) {
			buttons[i].setTypeface(typeface);
			buttons[i].setOnClickListener(this);
		}
		int[] array = dbAdapter.getActivIds(part);
		generateIds(array);
		position = 0;
		positive = 0;
		initDatasForViews(position);

		textTimer = (TextView) findViewById(R.id.timer);
		textTimer.setTypeface(FontFactory.getFont1(getApplicationContext()));
		time = new Timer(31 * 1000, 1000, textTimer);
		time.start();
		animButton = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.anim_button);
		animButton.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				buttons[trueAnswerID]
						.setBackgroundResource(R.drawable.question_button_correct);
				for (Button btn : buttons) {
					btn.setEnabled(false);
				}
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				for (Button btn : buttons) {
					btn.setEnabled(true);
				}
				buttons[trueAnswerID]
						.setBackgroundResource(R.drawable.bkg_answer);
				restart();
			}
		});

		animRepeat = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.anim_alpha_repeat_1);
		animRepeat2 = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.anim_alpha_repeat_2);
		animRepeat3 = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.anim_alpha_repeat_3);
		findViewById(R.id.helperFifty).startAnimation(animRepeat);
		findViewById(R.id.helperLife).startAnimation(animRepeat2);
		findViewById(R.id.helperExtraTime).startAnimation(animRepeat3);
		mediaPlayer = MediaPlayer.create(this, R.raw.clock_tick);
		mediaPlayer.setVolume(0.5f, 0.5f);
		mediaPlayer.setLooping(true);
		mediaPlayer.start();


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
	}
	private void generateIds(int[] array) {
		Generator generator = new Generator(ActivityPlay.this, quantity);
		generator.execute(array);
		try {
			ids = generator.get();
			for (int i = 0; i < array.length; i++) {
				Log.i(Main.LOG, (i + 1) + ")" + array[i]);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

	private int[] shuffle(int[] ar) {
		Random rnd = new Random();
		for (int i = ar.length - 1; i > 0; i--) {
			int index = rnd.nextInt(i + 1);
			int a = ar[index];
			ar[index] = ar[i];
			ar[i] = a;
		}
		return ar;
	}

	private int[] shuffle2(int needless) {
		int[] ar = shuffle(new int[] { 0, 1, 2, 3 });
		for (int i = 0; i < ar.length; i++)
			if (ar[i] == needless) {
				int a = ar[ar.length - 1];
				ar[ar.length - 1] = ar[i];
				ar[i] = a;
			}
		return new int[] { ar[0], ar[1] };
	}

	private void openDB() {
		dbAdapter = new DBAdapter(this);
		try {
			dbAdapter.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initDatasForViews(int pos) {
		int[] variants = shuffle(new int[] { 0, 1, 2, 3 });
		Cursor c = dbAdapter.getRecord(ids[pos]);
		if (c.moveToFirst()) {
			textQuestion.setText(c.getString(0));
			short i = 0;
			while (i < buttons.length) {
				if (variants[i] == 0)
					trueAnswerID = i;
				buttons[i].setTag(variants[i]);
				buttons[i].setText(c.getString(variants[i] + 1));
				i++;
			}
		}
	}

	private boolean key50 = false;

	// HELPERS
	public void onClickFifty(View v) {
		int[] falseAnswers = shuffle2(trueAnswerID);// random(2,trueAnswerID);
		for (int i = 0; i < falseAnswers.length; i++) {
			buttons[falseAnswers[i]].setVisibility(Button.INVISIBLE);
		}
		if (animRepeat != null)
			animRepeat.cancel();
		v.setAnimation(null);
		v.setVisibility(View.INVISIBLE);
		key50 = true;
	}

	public void onClickLife(View v) {
		if (animRepeat2 != null)
			animRepeat2.cancel();
		v.setAnimation(null);
		v.setVisibility(View.INVISIBLE);
		this.onClick(buttons[trueAnswerID]);
	}

	public void onClickExtraTime(View v) {
		if (animRepeat3 != null)
			animRepeat3.cancel();
		v.setAnimation(null);
		v.setVisibility(View.INVISIBLE);
		long millisFixed = time.getMillis();
		Log.d(Main.LOG, "millisFixed=" + millisFixed);
		time.cancel();
		time = new Timer(millisFixed + 15 * 1000, 1000, textTimer, millisFixed);
		time.start();

	}

	private void restart() {
		if (position >= quantity) {
			if (positive == position)
				dialogFinish(R.string.you_win);
			else
				dialogFinish(R.string.result);
			return;
		}
		time.cancel();
		time.start();
		initDatasForViews(position);
		if (key50) {
			for (Button btn : buttons) {
				btn.setVisibility(View.VISIBLE);
			}
			key50 = false;
		}
	}

	public void dialogFinish(int messageId) {
		Context context = ActivityPlay.this;
		final Dialog dialog = new Dialog(context);
		dialog.setCancelable(false);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_finish);
		dialog.setOnShowListener(new OnShowListener() {

			@Override
			public void onShow(DialogInterface dialog) {
				mediaPlayer.pause();
			}
		});
		dialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				mediaPlayer.start();
			}
		});
		TextView title = (TextView) dialog.findViewById(R.id.dialogTitle);
		title.setTypeface(FontFactory.getFont1(context));
		final double res = (double)positive/quantity * 100;
		if (R.string.result == messageId) {
		String msg = String.format("%s: %.2f",Main.getTranslate(getApplicationContext(),R.string.result),res)+"%";
			title.setText(msg);
		} else
			title.setText(Main.getTranslate(getApplicationContext(), messageId));
		ImageView close = (ImageView) dialog.findViewById(R.id.imageClose);
		close.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});

		Button buttonRestart = (Button) dialog
				.findViewById(R.id.btnDialogRestart);
		buttonRestart
				.setTypeface(FontFactory.getFont1(getApplicationContext()));
		buttonRestart.setText(Main.getTranslate(getApplicationContext(),
				R.string.restart));
		buttonRestart.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra(Main.INTENT_PART, part);
				intent.putExtra(Main.INTENT_SCORE, res);
				intent.putExtra(Main.INTENT_RESTART, true);
				if (dialog != null)
					dialog.dismiss();
				setResult(RESULT_OK, intent);
				finish();
				overridePendingTransition(0,0);
			}
		});

		Button buttonMainMenu = (Button) dialog
				.findViewById(R.id.btnDialogMainMenu);
		buttonMainMenu.setText(Main.getTranslate(getApplicationContext(),
				R.string.main_menu));
		buttonMainMenu.setTypeface(FontFactory
				.getFont1(getApplicationContext()));
		buttonMainMenu.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra(Main.INTENT_PART, part);
				intent.putExtra(Main.INTENT_SCORE, res);
				if (dialog != null)
					dialog.dismiss();
				setResult(RESULT_OK, intent);
				finish();
				overridePendingTransition(0,0);
			}
		});
		dialog.getWindow().setBackgroundDrawableResource(
				android.R.color.transparent);
		dialog.show();

	}

	@Override
	public void onClick(View v) {
		int tag = (Integer) v.getTag();
		position++;
		if (tag == 0) {
			positive++;
			animatioHorse();
		}
		v.startAnimation(animButton);
	}

	// TIMER

	// DOPOLNITELNO
	@Override
	public void onBackPressed() {
		dialogFinish(R.string.exit_game);
	}

	@Override
	protected void onStop() {
		Log.d(Main.LOG, "onStop");
		if (dbAdapter != null)
			dbAdapter.close();
		if (animButton != null)
			animButton.cancel();
		if (animRepeat != null)
			animRepeat.cancel();
		if (animRepeat2 != null)
			animRepeat2.cancel();
		if (animRepeat3 != null)
			animRepeat3.cancel();
		if (mediaPlayer.isPlaying())
			mediaPlayer.pause();
		if (time!=null)
			time.cancel();
			mediaPlayer.pause();
		mediaPlayer.release();
		ActivitySplitAnimationUtil.cancel();
		super.onStop();
	}

	int key = 0;
	private TranslateAnimation animTranslate;

	private void animatioHorse() {
		int delta = (int) (-getResources().getDimension(
				R.dimen.height_progressbar) / quantity);
		animTranslate = new TranslateAnimation(1.0f, 1.0f, key * delta,
				(key + 1) * delta);
		animTranslate.setDuration(1200);
		animTranslate.setFillAfter(true);
		animTranslate.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				int progress = positive * 100 / quantity;
				progressBar.setProgress(progress);
				if (positive % 3 == 0 && positive != 0) {
					int pos = positive / 3 - 1;
					textLevels[pos].setBackgroundColor(BROWN);
					textLevels[pos].setTextColor(WHITE);
					if (pos != 0) {
						textLevels[pos-1].setBackgroundColor(TRANSPARENT_WHITE);
						textLevels[pos-1].setTextColor(BROWN);
					}
				}
			}
		});
		imageHorse.startAnimation(animTranslate);
		key++;
	}

	private class Timer extends CountDownTimer {

		private TextView textView;
		private long millis;
		int[] drawables = new int[] { R.drawable.time_00, R.drawable.time_01,
				R.drawable.time_02, R.drawable.time_03, R.drawable.time_04,
				R.drawable.time_05, R.drawable.time_06, R.drawable.time_07,
				R.drawable.time_08, R.drawable.time_09, R.drawable.time_10 };
		private long millisFixed;

		public Timer(long millisInFuture, long countDownInterval,
				TextView textView) {
			super(millisInFuture, countDownInterval);
			this.textView = textView;
			this.millisFixed = millisInFuture;
		}

		public Timer(long millisInFuture, long countDownInterval,
				TextView textView, long millisFixed) {
			super(millisInFuture, countDownInterval);
			this.textView = textView;
			this.millisFixed = millisFixed;
			this.textView.setAlpha(0.7f);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			this.millis = millisUntilFinished;
			textView.setText("" + millisUntilFinished / 1000);
			if (millisFixed >= millisUntilFinished) {
				int pos = drawables.length - 1
						- (int) (millisUntilFinished / 3000);
				textView.setBackgroundResource(drawables[pos]);
				if (this.textView.getAlpha() != 1.0f) {
					this.textView.setAlpha(1.0f);
				}
			}
		}

		@Override
		public void onFinish() {
			textView.setBackgroundResource(R.drawable.time_11);
			textView.setText("0");
			dialogFinish(R.string.time_is_over);
		}

		public long getMillis() {
			return this.millis;
		}
	}

}
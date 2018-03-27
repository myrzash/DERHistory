package kz.nis.history.extra;

import kz.nis.history.Main;
import android.content.Context;
import android.graphics.Typeface;

public class FontFactory {
	private static String FONT1 = "vKorin.ttf";
	private static String FONT2 = "font_base.ttf";
	private static Typeface font1 = null;
	private static Typeface font2 = null;

	public static Typeface getFont1(Context context) {
		if (font1 == null) {
			font1 = Typeface.createFromAsset(context.getAssets(), FONT1);
		}
		return font1;
	}

	public static Typeface getFont2(Context context) {
		if (font2 == null) {
			font2 = Typeface.createFromAsset(context.getAssets(), FONT2);
		}
		return font2;
	}

	public static Typeface getBold(Context context) {
		if (Main.LANG == 0) {
			return getFont1(context);
		} else
			return getFont2(context);
	}
}

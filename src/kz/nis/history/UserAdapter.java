package kz.nis.history;

import kz.nis.history.extra.FontFactory;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class UserAdapter extends SimpleCursorAdapter {

	
	private int[] to;
	private Typeface typeface;

	public UserAdapter(Context context, int layout, Cursor c, String[] from,
			int[] to) {
		super(context, layout, c, from, to, 0);
		this.to = to;
		this.typeface = FontFactory.getFont1(context);
	}

	@Override
	public View getView(int pos, View view, ViewGroup group) {
		View v =  super.getView(pos, view, group);
		for(int id : to)
			((TextView)v.findViewById(id)).setTypeface(typeface);
		return v;
	}

}

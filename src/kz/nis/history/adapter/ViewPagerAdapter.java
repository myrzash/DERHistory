package kz.nis.history.adapter;

import java.text.MessageFormat;

import kz.nis.history.FragmentList;
import kz.nis.history.Main;
import kz.nis.history.R;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter{

	private String[] titles;
	private Context context;
//	private String[][] questions;
//	private String[][] answers;
////	private static  boolean update;
//	private static boolean[][] activs;


	public ViewPagerAdapter(Context context, FragmentManager fm) {
		super(fm);		
		this.context = context;
		this.titles = context.getResources().getStringArray(R.array.Categories);
	}

	@Override
	public Fragment getItem(int pos) {
		 return FragmentList.newInstance(context, pos);
	}
	
	@Override
	public int getCount() {
		return titles.length;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return  MessageFormat.format( titles[position], Main.LANG);
	}

	
}

package kz.nis.history;

import kz.nis.history.adapter.ViewPagerAdapter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class ActivityList extends FragmentActivity {
	private ViewPager viewPager;
	private ViewPagerAdapter pagerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		PagerTabStrip pagerStrip = (PagerTabStrip) findViewById(R.id.pagerStrip);
		pagerStrip.setTextSize(0,
				getResources().getDimension(R.dimen.text_size_high));
		viewPager = (ViewPager) findViewById(R.id.pager);
		pagerAdapter = new ViewPagerAdapter(getApplicationContext(),
				getSupportFragmentManager());
		viewPager.setAdapter(pagerAdapter);
		ImageView imageHome = (ImageView) findViewById(R.id.imageHome);
		imageHome.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}
}

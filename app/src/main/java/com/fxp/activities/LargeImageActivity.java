package com.fxp.activities;

import java.util.ArrayList;
import java.util.List;
import com.fxp.fragments.LargeImageFragment;
import com.moxun.tagcloud.R;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class LargeImageActivity extends FragmentActivity {
	ViewPager vp;
	private FragmentManager fm;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_largeimage);
		vp=(ViewPager) findViewById(R.id.vp);
		fm=getSupportFragmentManager();
		Intent intent = getIntent();
		int imgPosition = intent.getIntExtra("imgPosition", 0);
		Log.i("TEST", "LargeImageActivity-imgPosition"+imgPosition);
		@SuppressWarnings("unchecked")
		ArrayList<String> picList = (ArrayList<String>) intent.getSerializableExtra("picList");
		Log.i("TEST", "picList==null"+(picList==null));
		List<Fragment> data=new ArrayList<Fragment>();
		
		for(int i=0;i<picList.size();i++){
			Fragment myFragment = new LargeImageFragment();
			Bundle bundle = new Bundle();
			bundle.putString("picUrl",picList.get(i));
			myFragment.setArguments(bundle);
			data.add(myFragment);
		}
		
		vp.setAdapter(new MyAdapter(fm, data));
		vp.setCurrentItem(imgPosition);
		
		
	}
	class MyAdapter extends FragmentPagerAdapter{
		private List<Fragment> dataSource;
		public MyAdapter(FragmentManager fm,List<Fragment> dataSource) {
			super(fm);
			this.dataSource=dataSource;
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			return dataSource.get(arg0);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return dataSource.size();
		}
		
	}

}

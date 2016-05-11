package com.fxp.fragments;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.moxun.tagcloud.R;

public class LargeImageFragment extends Fragment {
	ImageView ivLarge;
	public static LargeImageFragment newInstance() {

		Bundle args = new Bundle();

		LargeImageFragment fragment = new LargeImageFragment();
		fragment.setArguments(args);
		return fragment;
	}
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_largeimage, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		ivLarge=(ImageView) view.findViewById(R.id.ivLarge);
		ivLarge.setOnClickListener(new BigimageOnClickListener());
		Bundle arguments = getArguments();
		String bitmapPath=arguments.getString("picUrl");
		ivLarge.setImageBitmap(BitmapFactory.decodeFile(bitmapPath));
	}
	public class BigimageOnClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			Log.i("TEST","BigimageOnClickListener");
			getActivity().finish();
			
		}
		
	}

}

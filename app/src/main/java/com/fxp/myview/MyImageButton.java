package com.fxp.myview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyImageButton extends LinearLayout {
	private ImageView imageViewbutton;
	private TextView textView;

	public MyImageButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		imageViewbutton = new ImageView(context, attrs);
		imageViewbutton.setPadding(0, 0, 0, 0);
		textView = new TextView(context, attrs);
		textView.setGravity(android.view.Gravity.CENTER);
		textView.setPadding(0, 0, 0, 0);
		setClickable(true);
		setFocusable(true);
//		setBackgroundResource(android.R.drawable.btn_default_small);
		setOrientation(LinearLayout.HORIZONTAL);
		setGravity(android.view.Gravity.CENTER);
		addView(imageViewbutton);
		addView(textView);
	}
}

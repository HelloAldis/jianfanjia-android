package com.jianfanjia.cn.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jianfanjia.cn.activity.R;

public class MainSiteLayout extends LinearLayout {

	ScrollLayout headLayout;
	RelativeLayout contentLayout;
	boolean intercept = true;

	private float lastX;
	private float lastY;

	public MainSiteLayout(Context context) {
		this(context, null, 0);
		// TODO Auto-generated constructor stub
	}

	public MainSiteLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public MainSiteLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		headLayout = (ScrollLayout) findViewById(R.id.site_scroller_layout);
		contentLayout = (RelativeLayout) findViewById(R.id.content_layout);
	}

}

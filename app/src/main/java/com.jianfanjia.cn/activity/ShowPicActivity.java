package com.jianfanjia.cn.activity;

import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.TextView;
import com.jianfanjia.cn.adapter.ShowPicPagerAdapter;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.interf.ViewPagerClickListener;
import com.jianfanjia.cn.tools.LogTool;

public class ShowPicActivity extends BaseActivity implements
		ViewPagerClickListener, OnPageChangeListener {
	private static final String TAG = ShowPicActivity.class.getName();
	private ViewPager viewPager;
	private ShowPicPagerAdapter showPicPagerAdapter;
	private TextView tipView;
	private List<String> imageList = new ArrayList<String>();
	private int currentPosition;// 当前第几张照片
	private int totalCount = 0;
	private String tipText = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			currentPosition = bundle.getInt(Constant.CURRENT_POSITION, 0);
			imageList = bundle.getStringArrayList(Constant.IMAGE_LIST);
			for (String str : imageList) {
				LogTool.d(this.getClass().getName(), " str:" + str);
			}
			totalCount = imageList.size();
		}
		showPicPagerAdapter = new ShowPicPagerAdapter(this, imageList, this);
		viewPager.setAdapter(showPicPagerAdapter);
		viewPager.setCurrentItem(currentPosition);
		viewPager.setOnPageChangeListener(this);
		setTipText();
	}

	private void setTipText() {
		tipText = (currentPosition + 1) + "/" + totalCount;
		tipView.setText(tipText);
	}

	@Override
	public void initView() {
		viewPager = (ViewPager) findViewById(R.id.showpicPager);
		tipView = (TextView) findViewById(R.id.pic_tip);
	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_showpic;
	}

	@Override
	public void onClickItem(int potition) {
		finish();
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0) {
		currentPosition = arg0;
		setTipText();
	}

}
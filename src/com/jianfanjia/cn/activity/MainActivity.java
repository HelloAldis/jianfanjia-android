package com.jianfanjia.cn.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v4.widget.SlidingPaneLayout.PanelSlideListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.fragment.MenuFragment;
import com.jianfanjia.cn.fragment.SiteManageFragment;

/**
 * 
 * @ClassName: MainActivity
 * @Description: 主界面
 * @author fengliang
 * @date 2015-8-18 下午1:28:28
 * 
 */
public class MainActivity extends BaseActivity implements PanelSlideListener {
	private static final String TAG = MainActivity.class.getClass().getName();
	private SlidingPaneLayout slidingPaneLayout;
	private MenuFragment menuFragment;
	private SiteManageFragment siteManageFragment;
	private DisplayMetrics displayMetrics = new DisplayMetrics();
	private int maxMargin = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void initView() {
		slidingPaneLayout = (SlidingPaneLayout) findViewById(R.id.slidingpanellayout);
		menuFragment = new MenuFragment();
		siteManageFragment = new SiteManageFragment();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.replace(R.id.slidingpane_menu, menuFragment);
		transaction.replace(R.id.slidingpane_content, siteManageFragment);
		transaction.commit();
		maxMargin = displayMetrics.heightPixels / 10;

	}

	@Override
	public void setListener() {
		slidingPaneLayout.setPanelSlideListener(this);
	}

	@Override
	public void onPanelSlide(View panel, float slideOffset) {
		Log.d(TAG, "slideOffset=" + slideOffset);
		int contentMargin = (int) (slideOffset * maxMargin);
		Log.d(TAG, "contentMargin=" + contentMargin);
	}

	@Override
	public void onPanelOpened(View panel) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPanelClosed(View panel) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(this.getClass().getName(), "---onDestroy()");
		// PushManager.getInstance().stopService(getApplicationContext());//
		// 完全终止SDK的服务
	}

	/**
	 * @return the slidingPaneLayout
	 */
	public SlidingPaneLayout getSlidingPaneLayout() {
		return slidingPaneLayout;
	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_main;
	}

}

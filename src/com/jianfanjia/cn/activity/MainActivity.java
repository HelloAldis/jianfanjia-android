package com.jianfanjia.cn.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v4.widget.SlidingPaneLayout.PanelSlideListener;
import android.util.Log;
import android.view.View;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.fragment.MenuFragment;
import com.jianfanjia.cn.fragment.SiteManageFragment;
import com.jianfanjia.cn.view.PagerEnabledSlidingPaneLayout;

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
	private PagerEnabledSlidingPaneLayout slidingPaneLayout = null;
	private MenuFragment menuFragment = null;
	private SiteManageFragment siteManageFragment = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void initView() {
		slidingPaneLayout = (PagerEnabledSlidingPaneLayout) findViewById(R.id.slidingpanellayout);
		menuFragment = new MenuFragment();
		siteManageFragment = new SiteManageFragment();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.replace(R.id.slidingpane_menu, menuFragment);
		transaction.replace(R.id.slidingpane_content, siteManageFragment);
		transaction.commit();
	}

	@Override
	public void setListener() {
		slidingPaneLayout.setPanelSlideListener(this);
	}

	@Override
	public void onPanelSlide(View panel, float slideOffset) {

	}

	@Override
	public void onPanelOpened(View panel) {

	}

	@Override
	public void onPanelClosed(View panel) {

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

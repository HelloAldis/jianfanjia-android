package com.jianfanjia.cn.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v4.widget.SlidingPaneLayout.PanelSlideListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.fragment.DesignMenuFragment;
import com.jianfanjia.cn.fragment.DesignerSiteManageFragment;
import com.jianfanjia.cn.fragment.OwnerMenuFragment;
import com.jianfanjia.cn.fragment.OwnerSiteManageFragment;
import com.jianfanjia.cn.tools.LogTool;
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
	private FrameLayout slidingpane_menu = null;
	private FrameLayout slidingpane_content = null;
	private OwnerMenuFragment ownerMenuFragment = null;
	private DesignMenuFragment designMenuFragment = null;
	private OwnerSiteManageFragment ownerSiteManageFragment = null;
	private DesignerSiteManageFragment designerSiteManageFragment = null;
	private String userIdentity = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void initView() {
		userIdentity = sharedPrefer.getValue(Constant.USERTYPE, null);
		LogTool.d(TAG, "userIdentity=" + userIdentity);
		slidingPaneLayout = (PagerEnabledSlidingPaneLayout) findViewById(R.id.slidingpanellayout);
		slidingpane_menu = (FrameLayout) findViewById(R.id.slidingpane_menu);
		slidingpane_content = (FrameLayout) findViewById(R.id.slidingpane_content);
		ownerMenuFragment = new OwnerMenuFragment();
		designMenuFragment = new DesignMenuFragment();
		ownerSiteManageFragment = new OwnerSiteManageFragment();
		designerSiteManageFragment = new DesignerSiteManageFragment();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		if (!TextUtils.isEmpty(userIdentity)) {
			if (userIdentity.equals(Constant.IDENTITY_OWNER)) {
				transaction.replace(R.id.slidingpane_menu, ownerMenuFragment);
				transaction.replace(R.id.slidingpane_content,
						ownerSiteManageFragment);
			} else if (userIdentity.equals(Constant.IDENTITY_DESIGNER)) {
				transaction.replace(R.id.slidingpane_menu, designMenuFragment);
				transaction.replace(R.id.slidingpane_content,
						designerSiteManageFragment);
			}
			transaction.commit();
		}
	}

	@Override
	public void setListener() {
		slidingPaneLayout.setPanelSlideListener(this);
	}

	@Override
	public void onPanelSlide(View panel, float slideOffset) {
		// slidingpane_menu.setScaleY(slideOffset / 2 + 0.5F);
		// slidingpane_menu.setScaleX(slideOffset / 2 + 0.5F);
		// slidingpane_menu.setAlpha(slideOffset);
		slidingpane_content.setScaleX(1 - slideOffset / 5);
		slidingpane_content.setScaleY(1 - slideOffset / 5);
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

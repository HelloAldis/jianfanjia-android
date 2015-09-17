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
import com.jianfanjia.cn.fragment.DesignerMenuFragment;
import com.jianfanjia.cn.fragment.OwnerMenuFragment;
import com.jianfanjia.cn.fragment.SiteManageFragment;
import com.jianfanjia.cn.layout.PagerEnabledSlidingPaneLayout;
import com.jianfanjia.cn.tools.LogTool;

/**
 * 
 * @ClassName: MainActivity
 * @Description: 主界面
 * @author fengliang
 * @date 2015-8-18 下午1:28:28
 * 
 */
public class MainActivity extends BaseActivity implements PanelSlideListener {
	private static final String TAG = MainActivity.class.getName();
	private PagerEnabledSlidingPaneLayout slidingPaneLayout = null;
	private FrameLayout slidingpane_content = null;
	private long mExitTime = 0L;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void initView() {
		slidingPaneLayout = (PagerEnabledSlidingPaneLayout) findViewById(R.id.slidingpanellayout);
		slidingpane_content = (FrameLayout) findViewById(R.id.slidingpane_content);
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		if (!TextUtils.isEmpty(userIdentity)) {
			if (userIdentity.equals(Constant.IDENTITY_OWNER)) {
				OwnerMenuFragment ownerMenuFragment = new OwnerMenuFragment();
				SiteManageFragment ownerSiteManageFragment = new SiteManageFragment();
				transaction.replace(R.id.slidingpane_menu, ownerMenuFragment);
				transaction.replace(R.id.slidingpane_content,
						ownerSiteManageFragment);
			} else if (userIdentity.equals(Constant.IDENTITY_DESIGNER)) {
				DesignerMenuFragment designerMenuFragment = new DesignerMenuFragment();
				SiteManageFragment ownerSiteManageFragment = new SiteManageFragment();
				transaction
						.replace(R.id.slidingpane_menu, designerMenuFragment);
				transaction.replace(R.id.slidingpane_content,
						ownerSiteManageFragment);
			} else {

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
		slidingpane_content.setScaleX(1 - slideOffset / 5);
		slidingpane_content.setScaleY(1 - slideOffset / 5);
	}

	@Override
	public void onPanelOpened(View panel) {
		Log.d(TAG, "---onPanelOpened()");
	}

	@Override
	public void onPanelClosed(View panel) {
		Log.d(TAG, "---onPanelClosed()");
	}

	@Override
	public void onBackPressed() {
		if (slidingPaneLayout.isOpen()) {
			slidingPaneLayout.closePane();
		} else {
			if ((System.currentTimeMillis() - mExitTime) > 2000) {// 如果两次按键时间间隔大于2000毫秒，则不退出
				makeTextLong("再按一次退出程序");
				mExitTime = System.currentTimeMillis();// 更新mExitTime
			} else {
				activityManager.exit();
			}
		}
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

	@Override
	public void onConnect() {
		LogTool.d(TAG, "onConnect()");
	}

	@Override
	public void onDisConnect() {
		LogTool.d(TAG, "onDisConnect()");
	}

}

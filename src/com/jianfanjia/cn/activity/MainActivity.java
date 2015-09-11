package com.jianfanjia.cn.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v4.widget.SlidingPaneLayout.PanelSlideListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import com.igexin.sdk.PushManager;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.Message;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.fragment.DesignerMenuFragment;
import com.jianfanjia.cn.fragment.OwnerMenuFragment;
import com.jianfanjia.cn.fragment.OwnerSiteManageFragment;
import com.jianfanjia.cn.interf.FragmentCallBack;
import com.jianfanjia.cn.interf.SwitchFragmentListener;
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
public class MainActivity extends BaseActivity implements PanelSlideListener,
		SwitchFragmentListener {
	private static final String TAG = MainActivity.class.getName();
	private FragmentCallBack callback = null;
	private PagerEnabledSlidingPaneLayout slidingPaneLayout = null;
	private FrameLayout slidingpane_content = null;
	private String userIdentity = null;

	@Override
	public void onAttachFragment(Fragment fragment) {
		super.onAttachFragment(fragment);
		try {
			callback = (FragmentCallBack) fragment;
		} catch (ClassCastException e) {
			LogTool.d(TAG, "e:" + e);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PushManager.getInstance().initialize(getApplicationContext());
	}

	@Override
	public void initView() {
		userIdentity = sharedPrefer.getValue(Constant.USERTYPE, null);
		LogTool.d(TAG, "userIdentity=" + userIdentity);
		slidingPaneLayout = (PagerEnabledSlidingPaneLayout) findViewById(R.id.slidingpanellayout);
		slidingpane_content = (FrameLayout) findViewById(R.id.slidingpane_content);
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		if (!TextUtils.isEmpty(userIdentity)) {
			if (userIdentity.equals(Constant.IDENTITY_OWNER)) {
				OwnerMenuFragment ownerMenuFragment = new OwnerMenuFragment();
				OwnerSiteManageFragment ownerSiteManageFragment = new OwnerSiteManageFragment();
				transaction.replace(R.id.slidingpane_menu, ownerMenuFragment);
				transaction.replace(R.id.slidingpane_content,
						ownerSiteManageFragment);
			} else if (userIdentity.equals(Constant.IDENTITY_DESIGNER)) {
				DesignerMenuFragment designerMenuFragment = new DesignerMenuFragment();
				OwnerSiteManageFragment ownerSiteManageFragment = new OwnerSiteManageFragment();
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

	}

	@Override
	public void onPanelClosed(View panel) {

	}

	@Override
	public void onBackPressed() {
		if (slidingPaneLayout.isOpen()) {
			moveTaskToBack(false);
		} else {
			slidingPaneLayout.openPane();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "---onDestroy()");
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
	public void switchFragment(int index) {
		LogTool.d(TAG, "index:" + index);
		callback.callBack(index);
	}

	@Override
	public void onReceiveMsg(Message message) {
		LogTool.d(TAG, "message=" + message);
		showNotify(message);
	}

}

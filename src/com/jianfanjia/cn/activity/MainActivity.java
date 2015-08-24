package com.jianfanjia.cn.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.fragment.SiteManageFragment;

/**
 * 
 * @ClassName: MainActivity
 * @Description: 主界面
 * @author fengliang
 * @date 2015-8-18 下午1:28:28
 * 
 */
public class MainActivity extends BaseActivity {
	
//	private SlidingMenu slidingMenu;
	private SiteManageFragment siteManageFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void initView() {
		/*slidingMenu = (SlidingMenu) this.findViewById(R.id.slidingmenulayout);
		slidingMenu.setMode(SlidingMenu.LEFT);
		slidingMenu.setTouchModeAbove(0);
		slidingMenu.setOnClosedListener(new OnClosedListener() {

			@Override
			public void onClosed() {
			}
		});
		slidingMenu.setOnOpenedListener(new OnOpenedListener() {

			@Override
			public void onOpened() {
			}
		});*/
		
		initMainContent();
	}

	private void initMainContent() {
		siteManageFragment = new SiteManageFragment();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.add(R.id.main_content_frame, siteManageFragment);
		fragmentTransaction.commit();		
	}

	@Override
	public void setListener() {
		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(this.getClass().getName(), "---onDestroy()");
		// PushManager.getInstance().stopService(getApplicationContext());//
		// 完全终止SDK的服务
	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_main;
	}

}

package com.jianfanjia.cn.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import com.jianfanjia.cn.base.BaseActivity;

/**
 * 
 * @ClassName: WelcomeActivity
 * @Description: »¶Ó­
 * @author fengliang
 * @date 2015-8-29 ÉÏÎç9:30:21
 * 
 */
public class WelcomeActivity extends BaseActivity {
	private Handler handler = new Handler();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	@Override
	public void initView() {
		handler.postDelayed(runnable, 2000);
	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub

	}

	private Runnable runnable = new Runnable() {

		@Override
		public void run() {
			startActivity(LoginActivity.class);
			finish();
		}

	};

	@Override
	public void onDestroy() {
		super.onDestroy();
		handler.removeCallbacks(runnable);
	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_welcome;
	}

}

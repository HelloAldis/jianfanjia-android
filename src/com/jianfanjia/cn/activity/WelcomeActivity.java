package com.jianfanjia.cn.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.tools.LogTool;

/**
 * 
 * @ClassName: WelcomeActivity
 * @Description: 欢迎
 * @author fengliang
 * @date 2015-8-29 上午9:30:21
 * 
 */
public class WelcomeActivity extends BaseActivity {
	private Handler handler = new Handler();
	private int first = 0;// 用于判断导航界面是否显示

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		first = sharedPrefer.getValue(Constant.ISFIRST, 0);
		LogTool.d(this.getClass().getName(), "first=" + first);
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
			if (first == 1) {
				startActivity(LoginActivity.class);
				finish();
			} else {
				startActivity(NavigateActivity.class);
				finish();
			}
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

package com.jianfanjia.cn.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.jianfanjia.cn.AppConfig;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.cache.DataCleanManager;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.http.HttpRestClient;
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
	private boolean isLoginExpire;//是否登录过去
	private boolean isLogin;// 是否登录过

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		first = sharedPrefer.getValue(Constant.ISFIRST, 0);
		isLogin = dataManager.isLogin();
		isLoginExpire = AppConfig.getInstance(this).isLoginExpire();
		LogTool.d(this.getClass().getName(), "first=" + first);
		DataCleanManager.cleanSharedPreference(this);//清理掉缓存的用户数据
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
				if (!isLogin) {
					startActivity(LoginActivity.class);
					finish();
				}else{
					if(!isLoginExpire){//登录未过期，添加cookies到httpclient记录身份
//						Log.i(this.getClass().getName(), appConfig.getCookies());
						DataCleanManager.cleanSharedPafrenceByName(WelcomeActivity.this, Constant.SHARED_MAIN);//清理掉缓存的用户数据
						startActivity(MainActivity.class);
						finish();
					}else{
						startActivity(LoginActivity.class);
						finish();
					}
				}
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

package com.jianfanjia.cn.activity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import com.jianfanjia.cn.AppConfig;
import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.http.LoadClientHelper;
import com.jianfanjia.cn.http.request.LoginRequest;
import com.jianfanjia.cn.interf.LoadDataListener;
import com.jianfanjia.cn.tools.LogTool;

/**
 * 
 * @ClassName: WelcomeActivity
 * @Description: ��ӭ
 * @author fengliang
 * @date 2015-8-29 ����9:30:21
 * 
 */
public class WelcomeActivity extends BaseActivity implements LoadDataListener {
	private Handler handler = new Handler();
	private boolean first;// �����жϵ��������Ƿ���ʾ
	private boolean isLoginExpire;// �Ƿ��¼��ȥ
	private boolean isLogin;// �Ƿ��¼��

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		first = dataManager.isFirst();
		isLogin = dataManager.isLogin();
		isLoginExpire = AppConfig.getInstance(this).isLoginExpire();
		LogTool.d(this.getClass().getName(), "first=" + first);
	}

	@Override
	public void initView() {
		handler.postDelayed(runnable, 3000);
	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadSuccess() {
		super.loadSuccess();
		startActivity(MainActivity.class);
		finish();
	}

	@Override
	public void loadFailture() {
		startActivity(LoginActivity.class);
		finish();
	}

	private Runnable runnable = new Runnable() {

		@Override
		public void run() {
			if (!first) {
				if (!isLogin) {
					Log.i(this.getClass().getName(), "û�е�¼");
					startActivity(LoginActivity.class);
					finish();
				} else {
					if (!isLoginExpire) {// ��¼δ���ڣ����cookies��httpclient��¼���
						Log.i(this.getClass().getName(), "δ����");
						startActivity(MainActivity.class);
						finish();
					} else {
						Log.i(this.getClass().getName(), "�Ѿ�����");
						MyApplication.getInstance().clearCookie();
						LoadClientHelper.login(
								WelcomeActivity.this,
								new LoginRequest(WelcomeActivity.this,
										dataManager.getAccount(), dataManager
												.getPassword()),
								WelcomeActivity.this);
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

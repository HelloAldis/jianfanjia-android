package com.jianfanjia.cn.activity;

import android.os.Bundle;
import android.util.Log;
import com.jianfanjia.cn.base.BaseActivity;

/**
 * 
 * @ClassName: MainActivity
 * @Description: ������
 * @author fengliang
 * @date 2015-8-18 ����1:28:28
 * 
 */
public class MainActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// PushManager.getInstance().initialize(getApplicationContext());
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(this.getClass().getName(), "---onDestroy()");
		// PushManager.getInstance().stopService(getApplicationContext());//
		// ��ȫ��ֹSDK�ķ���
	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_main;
	}

}

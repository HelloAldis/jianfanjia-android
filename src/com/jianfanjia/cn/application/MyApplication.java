package com.jianfanjia.cn.application;

import android.annotation.TargetApi;
import android.os.Build;
import com.jianfanjia.cn.base.BaseApplication;

/**
 * @version 1.0
 * @Description �������ҵ�Ӧ�ó�����
 * @author Administrator
 * @date 2015-8-20 ����1:45
 *
 */
public class MyApplication extends BaseApplication {
	private static MyApplication instance;
	private boolean isLogin;//�ж��û��Ƿ��¼
	private int userType = -1;//�ж��û�����
	
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressWarnings("unused")
	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
	}

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public boolean isLogin() {
		return isLogin;
	}

	public void setLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}

	public static MyApplication getInstance(){
		return instance;
	}
	
}

package com.jianfanjia.cn.application;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.StrictMode;

import com.jianfanjia.cn.base.BaseApplication;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.tools.SharedPrefer;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * @version 1.0
 * @Description 此类是我的应用程序类
 * @author Administrator
 * @date 2015-8-20 下午1:45
 *
 */
public class MyApplication extends BaseApplication {
	
	private static MyApplication instance;
	
	private boolean isLogin;//判断用户是否登录
	private int userType;//判断用户类型
	
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

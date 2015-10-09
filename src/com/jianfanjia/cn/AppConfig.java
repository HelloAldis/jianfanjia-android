package com.jianfanjia.cn;

import java.util.Calendar;
import android.content.Context;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.tools.SharedPrefer;

public class AppConfig {
	private static AppConfig appconfig;
	private SharedPrefer sharedPrefer;

	private AppConfig(Context context) {
		sharedPrefer = new SharedPrefer(context, Constant.SHARED_DATA);
	}

	public static AppConfig getInstance(Context context) {
		if (appconfig == null) {
			appconfig = new AppConfig(context);
		}
		return appconfig;
	}

	public void savaLastLoginTime(long loginTime) {
		sharedPrefer.setValue(Constant.LAST_LOGIN_TIME, loginTime);
	}

	// 是否登录信息已过期
	public boolean isLoginExpire() {
		long currentTime = Calendar.getInstance().getTimeInMillis();// 当前时间
		long loginLoginTime = sharedPrefer.getValue(Constant.LAST_LOGIN_TIME,
				currentTime);
		if (currentTime - loginLoginTime > Constant.LOGIN_EXPIRE) {
			return true;
		}
		return false;
	}

}

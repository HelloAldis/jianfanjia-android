package com.jianfanjia.cn;

import java.util.Calendar;
import android.content.Context;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.tools.SharedPrefer;

public class AppConfig {
	private static AppConfig appconfig;
	private SharedPrefer sharedPrefer;

	private AppConfig(Context context) {
		sharedPrefer = new SharedPrefer(context, Constant.SHARED_MAIN);
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

	// �Ƿ��¼��Ϣ�ѹ���
	public boolean isLoginExpire() {
		long currentTime = Calendar.getInstance().getTimeInMillis();// ��ǰʱ��
		long loginLoginTime = sharedPrefer.getValue(Constant.LAST_LOGIN_TIME,
				currentTime);
		if (currentTime - loginLoginTime > Constant.LOGIN_EXPIRE) {
			return true;
		}
		return false;
	}

}

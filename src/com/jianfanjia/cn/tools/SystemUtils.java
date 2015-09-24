package com.jianfanjia.cn.tools;

import java.util.List;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.jianfanjia.cn.activity.NotifyActivity;

public class SystemUtils {
	/**
	 * �ж�Ӧ���Ƿ��Ѿ�����
	 * 
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static boolean isAppAlive(Context context, String packageName) {
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> processInfos = activityManager
				.getRunningAppProcesses();
		for (int i = 0; i < processInfos.size(); i++) {
			if (processInfos.get(i).processName.equals(packageName)) {
				Log.i("NotificationLaunch", String.format(
						"the %s is running, isAppAlive return true",
						packageName));
				return true;
			}
		}
		Log.i("NotificationLaunch", String.format(
				"the %s is not running, isAppAlive return false", packageName));
		return false;
	}

	public static void startDetailActivity(Context context, String name,
			String price, String detail) {
		Intent intent = new Intent(context, NotifyActivity.class);
		intent.putExtra("name", name);
		intent.putExtra("price", price);
		intent.putExtra("detail", detail);
		context.startActivity(intent);
	}
}

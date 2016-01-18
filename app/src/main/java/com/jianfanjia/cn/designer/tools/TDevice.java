package com.jianfanjia.cn.designer.tools;

import android.app.ActivityManager;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.jianfanjia.cn.designer.application.MyApplication;

import java.util.List;

public class TDevice {

	/**
	 * 判断应用是否已经启动
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
	
	  public static DisplayMetrics getDisplayMetrics() {
	        DisplayMetrics displaymetrics = new DisplayMetrics();
	        ((WindowManager) MyApplication.getInstance().getSystemService(
	                Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(
	                displaymetrics);
	        return displaymetrics;
	    }

	    public static float getScreenHeight() {
	        return getDisplayMetrics().heightPixels;
	    }

	    public static float getScreenWidth() {
	        return getDisplayMetrics().widthPixels;
	    }

	/**
	 * 用于获取状态栏的高度。 使用Resource对象获取
	 *
	 * @return 返回状态栏高度的像素值。
	 */
	public static int getStatusBarHeight(Context context) {
		int result = 0;
		int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
				"android");
		if (resourceId > 0) {
			result = context.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

}

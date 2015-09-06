package com.jianfanjia.cn.view.dialog;

import com.jianfanjia.cn.activity.R;

import android.app.Activity;
import android.content.Context;

public class DialogHelper {
	
	public static WaitDialog getWaitDialog(Activity activity, int message) {
		WaitDialog dialog = null;
		try {
			dialog = new WaitDialog(activity, R.style.dialog);
			dialog.setMessage(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dialog;
	}

	public static WaitDialog getWaitDialog(Activity activity, String message) {
		WaitDialog dialog = null;
		try {
			dialog = new WaitDialog(activity, R.style.dialog);
			dialog.setMessage(message);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return dialog;
	}

	public static WaitDialog getCancelableWaitDialog(Activity activity,
			String message) {
		WaitDialog dialog = null;
		try {
			dialog = new WaitDialog(activity, R.style.dialog);
			dialog.setMessage(message);
			dialog.setCancelable(true);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return dialog;
	}

}

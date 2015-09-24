package com.jianfanjia.cn.view.dialog;

import android.app.Activity;
import android.content.Context;

import com.jianfanjia.cn.activity.R;

public class DialogHelper {

	public static CommonDialog getPinterestDialog(Context context) {
		return new CommonDialog(context, R.style.common_dialog);
	}

	public static CommonDialog getPinterestDialogCancelable(Context context) {
		CommonDialog dialog = new CommonDialog(context, R.style.common_dialog);
		dialog.setCanceledOnTouchOutside(true);
		return dialog;
	}

	public static WaitDialog getWaitDialog(Activity activity, int message) {
		WaitDialog dialog = null;
		try {
			dialog = new WaitDialog(activity, R.style.progress_dialog);
			dialog.setMessage(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dialog;
	}

	public static WaitDialog getWaitDialog(Activity activity, String message) {
		WaitDialog dialog = null;
		try {
			dialog = new WaitDialog(activity, R.style.progress_dialog);
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
			dialog = new WaitDialog(activity, R.style.progress_dialog);
			dialog.setMessage(message);
			dialog.setCancelable(true);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return dialog;
	}

}

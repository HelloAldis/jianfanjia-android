package com.jianfanjia.cn.tools;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import com.jianfanjia.cn.interf.DialogListener;

public class DialogTool {
	public static ProgressDialog showProgressDialog(Context context,
			String mssage) {
		ProgressDialog progressDialog = new ProgressDialog(context);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage(mssage);
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false); // ���dialog��������λ�ã���ȡ��dialog
		return progressDialog;
	}

	public static void showDialog(final Context context, String title,
			String message, String msg1, String msg2,
			final DialogListener listener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title).setMessage(message).setCancelable(false)
				.setPositiveButton(msg1, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						listener.onPositiveButtonClick();
					}
				})
				.setNegativeButton(msg2, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						listener.onNegativeButtonClick();
					}
				}).create().show();
	}

	public static void showTips(final Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setTitle("û�п�������");
		builder.setMessage("��ǰ���粻���ã��Ƿ��������磿");
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// ���û���������ӣ�������������ý���
				context.startActivity(new Intent(
						Settings.ACTION_WIRELESS_SETTINGS));
			}
		});
		builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		builder.create();
		builder.show();
	}

}

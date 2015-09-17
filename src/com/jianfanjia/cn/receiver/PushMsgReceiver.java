package com.jianfanjia.cn.receiver;

import org.apache.http.Header;
import org.json.JSONObject;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import com.google.gson.Gson;
import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;
import com.jianfanjia.cn.activity.NotifyActivity;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.bean.NotifyMessage;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.http.JianFanJiaApiClient;
import com.jianfanjia.cn.tools.LogTool;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * 
 * @ClassName: PushMsgReceiver
 * @Description: ������Ϣ�����㲥
 * @author fengliang
 * @date 2015-8-18 ����1:34:52
 * 
 */
public class PushMsgReceiver extends BroadcastReceiver {
	private static final String TAG = PushMsgReceiver.class.getName();
	private Gson gson = new Gson();

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		LogTool.d(TAG, "onReceive() action=" + bundle.getInt("action"));
		switch (bundle.getInt(PushConsts.CMD_ACTION)) {
		case PushConsts.GET_MSG_DATA:
			// ��ȡ͸������
			// String appid = bundle.getString("appid");
			byte[] payload = bundle.getByteArray("payload");
			String taskid = bundle.getString("taskid");
			String messageid = bundle.getString("messageid");
			// smartPush��������ִ���ýӿڣ�actionid��ΧΪ90000-90999���ɸ���ҵ�񳡾�ִ��
			boolean result = PushManager.getInstance().sendFeedbackMessage(
					context, taskid, messageid, 90001);
			LogTool.d(TAG, "��������ִ�ӿڵ���" + (result ? "�ɹ�" : "ʧ��"));
			if (payload != null) {
				String data = new String(payload);
				LogTool.d(TAG, "receiver payload : " + data);
				parseMessage(context, data);
			}
			break;
		case PushConsts.GET_CLIENTID:
			// ��ȡClientID(CID)
			// ������Ӧ����Ҫ��CID�ϴ��������������������ҽ���ǰ�û��ʺź�CID���й������Ա��պ�ͨ���û��ʺŲ���CID������Ϣ����
			String cid = bundle.getString("clientid");
			LogTool.d(TAG, "cid:" + cid);
			if (!TextUtils.isEmpty(cid)) {
				uploadClientID(context, cid);
			}
			break;
		case PushConsts.THIRDPART_FEEDBACK:
			/*
			 * String appid = bundle.getString("appid"); String taskid =
			 * bundle.getString("taskid"); String actionid =
			 * bundle.getString("actionid"); String result =
			 * bundle.getString("result"); long timestamp =
			 * bundle.getLong("timestamp");
			 * 
			 * Log.d("GetuiSdkDemo", "appid = " + appid); Log.d("GetuiSdkDemo",
			 * "taskid = " + taskid); Log.d("GetuiSdkDemo", "actionid = " +
			 * actionid); Log.d("GetuiSdkDemo", "result = " + result);
			 * Log.d("GetuiSdkDemo", "timestamp = " + timestamp);
			 */
			break;
		default:
			break;
		}
	}

	/**
	 * ��������͸����Ϣ
	 * 
	 * @param message
	 */
	private void parseMessage(Context context, String jsonStr) {
		try {
			NotifyMessage message = gson.fromJson(jsonStr, NotifyMessage.class);
			Log.i(TAG, "message:" + message);
			sendNotifycation(context, message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * �ϴ�clientid
	 * 
	 * @param context
	 * @param account
	 * @param clientId
	 */
	private void uploadClientID(Context context, String clientId) {
		JianFanJiaApiClient.uploadRegisterId(context, clientId,
				new JsonHttpResponseHandler() {
					@Override
					public void onStart() {
						LogTool.d(TAG, "onStart()");
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						LogTool.d(TAG, "JSONObject response:" + response);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						LogTool.d(TAG,
								"Throwable throwable:" + throwable.toString());
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						LogTool.d(TAG, "throwable:" + throwable);
					};
				});
	}

	/**
	 * Notification
	 * 
	 * @param message
	 */
	protected void sendNotifycation(Context context, NotifyMessage message) {
		NotificationManager nManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				context);
		builder.setSmallIcon(R.drawable.ic_launcher);
		String type = message.getType();
		if (type.equals(Constant.CAIGOU_NOTIFY)) {
			builder.setTicker(context.getResources().getText(
					R.string.caigouText));
			builder.setContentTitle(context.getResources().getText(
					R.string.caigouText));
		} else if (type.equals(Constant.FUKUAN_NOTIFY)) {
			builder.setTicker(context.getResources().getText(
					R.string.fukuanText));
			builder.setContentTitle(context.getResources().getText(
					R.string.fukuanText));
		} else if (type.equals(Constant.YANQI_NOTIFY)) {
			builder.setTicker(context.getResources()
					.getText(R.string.yanqiText));
			builder.setContentTitle(context.getResources().getText(
					R.string.yanqiText));
		}
		builder.setContentText(message.getContent());
		builder.setWhen(System.currentTimeMillis());
		builder.setPriority(Notification.PRIORITY_DEFAULT);
		builder.setAutoCancel(true);
		Intent intent = new Intent(context, NotifyActivity.class);
		intent.putExtra("Type", type);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		builder.setContentIntent(pendingIntent);
		Notification notification = builder.build();
		notification.sound = Uri.parse("android.resource://"
				+ context.getPackageName() + "/" + R.raw.message);
		nManager.notify(Constant.NotificationID, notification);
	}
}

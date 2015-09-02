package com.jianfanjia.cn.receiver;

import java.util.List;
import org.apache.http.Header;
import org.json.JSONObject;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;
import com.jianfanjia.cn.bean.Message;
import com.jianfanjia.cn.http.JianFanJiaApiClient;
import com.jianfanjia.cn.inter.manager.ListenerManeger;
import com.jianfanjia.cn.interf.PushMsgReceiveListener;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * 
 * @ClassName: PushMsgReceiver
 * @Description: 推送消息监听广播
 * @author fengliang
 * @date 2015-8-18 下午1:34:52
 * 
 */
public class PushMsgReceiver extends BroadcastReceiver {
	private static final String TAG = "PushMsgReceiver";
	private ListenerManeger listenerManeger = null;

	@Override
	public void onReceive(Context context, Intent intent) {
		listenerManeger = ListenerManeger.getListenerManeger();
		// -------------------------------------------------------------------
		Bundle bundle = intent.getExtras();
		LogTool.d(TAG, "onReceive() action=" + bundle.getInt("action"));
		switch (bundle.getInt(PushConsts.CMD_ACTION)) {
		case PushConsts.GET_MSG_DATA:
			// 获取透传数据
			// String appid = bundle.getString("appid");
			byte[] payload = bundle.getByteArray("payload");
			String taskid = bundle.getString("taskid");
			String messageid = bundle.getString("messageid");
			// smartPush第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
			boolean result = PushManager.getInstance().sendFeedbackMessage(
					context, taskid, messageid, 90001);
			LogTool.d(TAG, "第三方回执接口调用" + (result ? "成功" : "失败"));
			if (payload != null) {
				String data = new String(payload);
				LogTool.d(TAG, "receiver payload : " + data);
				parseMessage(data);
			}
			break;
		case PushConsts.GET_CLIENTID:
			// 获取ClientID(CID)
			// 第三方应用需要将CID上传到第三方服务器，并且将当前用户帐号和CID进行关联，以便日后通过用户帐号查找CID进行消息推送
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
	 * 解析推送透传消息
	 * 
	 * @param message
	 */
	private void parseMessage(String jsonStr) {
		try {
			Message message = JsonParser.jsonToBean(jsonStr, Message.class);
			List<PushMsgReceiveListener> listeners = ListenerManeger.msgListeners;
			for (PushMsgReceiveListener listener : listeners) {
				LogTool.d(TAG, "listener:" + listener);
				listener.onReceiveMsg(message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 消息提醒
	 * 
	 * @param message
	 */
	private void showNotify(Message message) {

	}

	/**
	 * 上传clientid
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
}

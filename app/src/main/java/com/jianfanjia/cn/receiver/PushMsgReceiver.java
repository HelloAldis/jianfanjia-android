package com.jianfanjia.cn.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;
import com.jianfanjia.cn.activity.MyProcessDetailActivity;
import com.jianfanjia.cn.bean.NotifyMessage;
import com.jianfanjia.cn.cache.DataManagerNew;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.dao.impl.NotifyMessageDao;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.interf.ReceiveMsgListener;
import com.jianfanjia.cn.interf.manager.ListenerManeger;
import com.jianfanjia.cn.tools.DaoManager;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.SystemUtils;
import com.jianfanjia.cn.tools.UiHelper;

/**
 * Description:推送消息监听广播
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 15:30
 */
public class PushMsgReceiver extends BroadcastReceiver {
    private static final String TAG = PushMsgReceiver.class.getName();
    private ListenerManeger listenerManeger = null;
    private NotifyMessageDao notifyMessageDao = null;
    private DataManagerNew dataManager = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        listenerManeger = ListenerManeger.getListenerManeger();
        notifyMessageDao = DaoManager.getNotifyMessageDao(context);
        dataManager = DataManagerNew.getInstance();
        //-------------------------------------------------
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
                    parseMessage(context, data);
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

    //解析推送透传消息
    private void parseMessage(Context context, String jsonStr) {
        try {
            NotifyMessage message = JsonParser.jsonToBean(jsonStr,
                    NotifyMessage.class);
            Log.i(TAG, "message:" + message);
            notifyMessageDao.save(message);
            if (SystemUtils.isAppAlive(context, context.getPackageName())) {
                LogTool.d(TAG, "the app process is alive");
                ReceiveMsgListener listener = listenerManeger
                        .getReceiveMsgListener();
                Log.i(TAG, "listener:" + listener);
                if (null != listener) {
                    if (listener instanceof MyProcessDetailActivity) {
                        listener.onReceive(message);
                    }
                } else {
                    UiHelper.sendNotifycation(context, message);
                }
            } else {
                LogTool.d(TAG, "the app process is dead");
                Intent launchIntent = context.getPackageManager()
                        .getLaunchIntentForPackage(context.getPackageName());
                launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                Bundle args = new Bundle();
                args.putString("Type", message.getType());
                launchIntent.putExtra(Constant.EXTRA_BUNDLE, args);
                context.startActivity(launchIntent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传clientid
     *
     * @param context
     * @param clientId
     */
    private void uploadClientID(Context context, String clientId) {
        JianFanJiaClient.uploadRegisterId(context, clientId, new ApiUiUpdateListener() {
            @Override
            public void preLoad() {

            }

            @Override
            public void loadSuccess(Object data) {
                LogTool.d(TAG, "data:" + data);
            }

            @Override
            public void loadFailture(String error_msg) {
                LogTool.d(TAG, "error_msg:" + error_msg);
            }
        }, this);
    }

}

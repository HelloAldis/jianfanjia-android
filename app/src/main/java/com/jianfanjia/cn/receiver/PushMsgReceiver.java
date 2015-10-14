package com.jianfanjia.cn.receiver;

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

import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;
import com.jianfanjia.cn.activity.CheckActivity;
import com.jianfanjia.cn.activity.MainActivity;
import com.jianfanjia.cn.activity.NotifyActivity;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.BaseResponse;
import com.jianfanjia.cn.bean.NotifyMessage;
import com.jianfanjia.cn.bean.ProcessInfo;
import com.jianfanjia.cn.bean.SectionInfo;
import com.jianfanjia.cn.cache.DataManagerNew;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.dao.impl.NotifyMessageDao;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.LoadDataListener;
import com.jianfanjia.cn.interf.manager.ListenerManeger;
import com.jianfanjia.cn.tools.DaoManager;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;

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
        JianFanJiaClient.uploadRegisterId(context, clientId, new LoadDataListener() {
            @Override
            public void preLoad() {

            }

            @Override
            public void loadSuccess(BaseResponse baseResponse) {
                LogTool.d(TAG, "baseResponse:" +baseResponse.toString());
            }

            @Override
            public void loadFailture() {

            }
        }, this);
    }

    private void sendNotifycation(Context context, NotifyMessage message) {
        int notifyId = -1;
        NotificationManager nManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                context);
        builder.setSmallIcon(R.mipmap.icon_notify);
        String type = message.getType();
        PendingIntent pendingIntent = null;
        if (type.equals(Constant.YANQI_NOTIFY)) {
            notifyId = Constant.YANQI_NOTIFY_ID;
            builder.setTicker(context.getResources()
                    .getText(R.string.yanqiText));
            builder.setContentTitle(context.getResources().getText(
                    R.string.yanqiText));
            Intent mainIntent = new Intent(context, MainActivity.class);
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            Intent notifyIntent = new Intent(context, NotifyActivity.class);
            notifyIntent.putExtra("Type", type);
            Intent[] intents = {mainIntent, notifyIntent};
            pendingIntent = PendingIntent.getActivities(context, 0, intents,
                    PendingIntent.FLAG_UPDATE_CURRENT);
        } else if (type.equals(Constant.FUKUAN_NOTIFY)) {
            notifyId = Constant.FUKUAN_NOTIFY_ID;
            builder.setTicker(context.getResources().getText(
                    R.string.fukuanText));
            builder.setContentTitle(context.getResources().getText(
                    R.string.fukuanText));
            Intent mainIntent = new Intent(context, MainActivity.class);
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            Intent notifyIntent = new Intent(context, NotifyActivity.class);
            notifyIntent.putExtra("Type", type);
            Intent[] intents = {mainIntent, notifyIntent};
            pendingIntent = PendingIntent.getActivities(context, 0, intents,
                    PendingIntent.FLAG_UPDATE_CURRENT);
        } else if (type.equals(Constant.CAIGOU_NOTIFY)) {
            notifyId = Constant.CAIGOU_NOTIFY_ID;
            builder.setTicker(context.getResources().getText(
                    R.string.caigouText));
            builder.setContentTitle(context.getResources().getText(
                    R.string.caigouText));
            Intent mainIntent = new Intent(context, MainActivity.class);
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            Intent notifyIntent = new Intent(context, NotifyActivity.class);
            notifyIntent.putExtra("Type", type);
            Intent[] intents = {mainIntent, notifyIntent};
            pendingIntent = PendingIntent.getActivities(context, 0, intents,
                    PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            notifyId = Constant.YANSHOU_NOTIFY_ID;
            ProcessInfo processInfo = dataManager.getDefaultProcessInfo();
            SectionInfo sectionInfo = processInfo.getSectionInfoByName(message
                    .getSection());
            LogTool.d(TAG, "processInfo:" + processInfo + " sectionInfo:"
                    + sectionInfo);
            builder.setTicker(context.getResources().getText(
                    R.string.yanshouText));
            builder.setContentTitle(context.getResources().getText(
                    R.string.yanshouText));
            Intent mainIntent = new Intent(context, MainActivity.class);
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            Intent checkIntent = new Intent(context, CheckActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(Constant.PROCESS_NAME, sectionInfo.getName());
            bundle.putInt(Constant.PROCESS_STATUS, sectionInfo.getStatus());
            checkIntent.putExtras(bundle);
            Intent[] intents = {mainIntent, checkIntent};
            pendingIntent = PendingIntent.getActivities(context, 0, intents,
                    PendingIntent.FLAG_UPDATE_CURRENT);
        }
        builder.setContentText(message.getContent());
        builder.setWhen(System.currentTimeMillis());
        builder.setAutoCancel(true);
        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        notification.sound = Uri.parse("android.resource://"
                + context.getPackageName() + "/" + R.raw.message);
        nManager.notify(notifyId, notification);
    }


}

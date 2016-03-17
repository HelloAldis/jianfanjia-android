package com.jianfanjia.cn.designer.tools;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.jianfanjia.cn.designer.AppManager;
import com.jianfanjia.cn.designer.Event.MessageCountEvent;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.activity.MainActivity;
import com.jianfanjia.cn.designer.activity.common.CommentListActivity_;
import com.jianfanjia.cn.designer.activity.my.NoticeDetailActivity;
import com.jianfanjia.cn.designer.bean.NotifyMessage;
import com.jianfanjia.cn.designer.config.Constant;
import com.jianfanjia.cn.designer.config.Global;
import de.greenrobot.event.EventBus;

/**
 * Description: com.jianfanjia.cn.tools
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-03-10 15:15
 */
public class MessageUtil {
    private static final String TAG = "MessageUtil";

    //解析推送透传消息
    public static void parseMessage(Context context, String jsonStr) {
        try {
            NotifyMessage message = JsonParser.jsonToBean(jsonStr,
                    NotifyMessage.class);
            Log.i(TAG, "message:" + message);
            sendNotifycation(context, message);
            EventBus.getDefault().post(new MessageCountEvent());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void sendNotifycation(Context context, NotifyMessage message) {
        int notifyId = (int) System.currentTimeMillis();
        LogTool.d(TAG, "notifyId =" + notifyId);
        NotificationManager nManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                context);
        RemoteViews mRemoteViews = new RemoteViews(context.getPackageName(), R.layout.view_custom_notify);
        mRemoteViews.setImageViewResource(R.id.list_item_img, R.mipmap.icon_notify);
        builder.setSmallIcon(R.mipmap.icon_notify_small);
        String type = message.getType();
        LogTool.d(TAG, "type =" + type);
        Intent mainIntent = new Intent(context, MainActivity.class);
        Intent targetIntent = null;
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (message.getType().equals(Constant.TYPE_SECTION_COMMENT_MSG) || message.getType().equals(Constant
                .TYPE_PLAN_COMMENT_MSG)) {
            targetIntent = new Intent(context, CommentListActivity_.class);
        } else {
            targetIntent = new Intent(context, NoticeDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(Global.MSG_ID, message.getMessageid());
            targetIntent.putExtras(bundle);
        }
        PendingIntent pendingIntent = null;
        if (AppManager.getAppManager().getActivity(MainActivity.class) == null) {
            Intent[] intents = {mainIntent, targetIntent};
            pendingIntent = PendingIntent.getActivities(context, notifyId, intents,
                    PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            pendingIntent = PendingIntent.getActivity(context, notifyId, targetIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
        }
        builder.setTicker(context.getResources().getText(R.string.app_name));
        mRemoteViews.setTextViewText(R.id.list_item_title, context.getResources().getText(R.string.app_name));
        mRemoteViews.setTextViewText(R.id.list_item_date, DateFormatTool.toLocalTimeString(message.getTime()));
        mRemoteViews.setTextViewText(R.id.list_item_content, message.getContent());
        builder.setContent(mRemoteViews);
        builder.setWhen(System.currentTimeMillis());
        builder.setAutoCancel(true);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        notification.vibrate = new long[]{0, 300, 500, 700};
        notification.sound = Uri.parse("android.resource://"
                + context.getPackageName() + "/" + R.raw.message);
        nManager.notify(notifyId, notification);
    }

}

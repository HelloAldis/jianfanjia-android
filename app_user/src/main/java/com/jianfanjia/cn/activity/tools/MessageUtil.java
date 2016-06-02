package com.jianfanjia.cn.activity.tools;

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

import com.jianfanjia.cn.activity.ui.activity.my.NoticeDetailActivity;
import com.jianfanjia.api.model.NotifyMessage;
import com.jianfanjia.cn.activity.AppManager;
import com.jianfanjia.cn.activity.ui.Event.MessageCountEvent;
import com.jianfanjia.cn.activity.ui.activity.MainActivity;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.ui.activity.common.CommentListActivity;
import com.jianfanjia.cn.activity.config.Constant;
import com.jianfanjia.cn.activity.constant.IntentConstant;
import com.jianfanjia.common.tool.DateFormatTool;
import com.jianfanjia.common.tool.LogTool;

import de.greenrobot.event.EventBus;

import static com.jianfanjia.common.tool.JsonParser.jsonToBean;

/**
 * Description: com.jianfanjia.cn.tools
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-03-10 15:15
 */
public class MessageUtil {
    private static final String TAG = MessageUtil.class.getName();

    //解析推送透传消息
    public static void parseMessage(Context context, String jsonStr) {
        try {
            NotifyMessage message = jsonToBean(jsonStr,
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
        mRemoteViews.setImageViewResource(R.id.list_item_img, R.drawable.ic_launcher);
        builder.setSmallIcon(R.mipmap.icon_notify_small);
        String type = message.getType();
        LogTool.d(TAG, "type =" + type);
        Intent mainIntent = new Intent(context, MainActivity.class);
        Intent targetIntent = null;
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (type.equals(Constant.TYPE_SECTION_COMMENT_MSG) || type.equals(Constant
                .TYPE_PLAN_COMMENT_MSG)) {
            targetIntent = new Intent(context, CommentListActivity.class);
        } else {
            targetIntent = new Intent(context, NoticeDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(IntentConstant.MSG_ID, message.getMessageid());
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
        mRemoteViews.setTextViewText(R.id.list_item_date, DateFormatTool.getHumReadDateString(message.getTime()));
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

package com.jianfanjia.cn.tools;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.jianfanjia.cn.activity.MainActivity;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.common.CommentListActivity_;
import com.jianfanjia.cn.activity.my.NoticeActivity;
import com.jianfanjia.cn.bean.NotifyMessage;
import com.jianfanjia.cn.config.Constant;

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
           /* if (TDevice.isAppAlive(context, context.getPackageName())) {
                LogTool.d(TAG, "the app process is alive");

                Activity activity = AppManager.getAppManager().currentActivity();
                String processId = null;
                String messageProcessId = null;
                //此处的显示策略是：只有当MyProcessDetailActivity在当前屏幕，并且窗口聚焦，并且推送的Message的processid与MyProcessDetailActivity的processid相同的
                // 情况下才会显示对话框，其他任何形式的通知都弹出通知栏
                if (activity != null &&
                        activity instanceof MyProcessDetailActivity
                        && !TextUtils.isEmpty(processId = ((MyProcessDetailActivity) activity).getProcessId())
                        && !TextUtils.isEmpty(messageProcessId = message.getProcessid())
                        && messageProcessId.equals(processId)
                        && activity.hasWindowFocus()) {
                    LogTool.d(TAG, "MyProcessDetailActivity is resume");
                    ((MyProcessDetailActivity) activity).onReceive(message);
                } else {
                    LogTool.d(TAG, "MyProcessDetailActivity is not in stakes");
                    UiHelper.sendNotifycation(context, message);
                }
            } else {
                LogTool.d(TAG, "the app process is dead");
                Intent launchIntent = context.getPackageManager()
                        .getLaunchIntentForPackage(context.getPackageName());
                launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                launchIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//                Bundle args = new Bundle();
//                args.putString("Type", message.getType());
//                launchIntent.putExtra(Constant.EXTRA_BUNDLE, args);
                context.startActivity(launchIntent);
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendNotifycation(Context context, NotifyMessage message) {
        int notifyId = -1;
        NotificationManager nManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                context);
        RemoteViews mRemoteViews = new RemoteViews(context.getPackageName(), R.layout.view_custom_notify);
        mRemoteViews.setImageViewResource(R.id.list_item_img, R.mipmap.icon_notify);
        builder.setSmallIcon(R.mipmap.icon_notify_small);
        String type = message.getType();
        LogTool.d(TAG, "type =" + type);
        PendingIntent pendingIntent = null;
        if (type.equals(Constant.TYPE_DELAY_MSG)) {
            notifyId = Constant.YANQI_NOTIFY_ID;
        } else if (type.equals(Constant.TYPE_PAY_MSG)) {
            notifyId = Constant.FUKUAN_NOTIFY_ID;
        } else if (type.equals(Constant.TYPE_CAIGOU_MSG)) {
            notifyId = Constant.CAIGOU_NOTIFY_ID;
        } else if (type.equals(Constant.TYPE_CONFIRM_CHECK_MSG)) {
            notifyId = Constant.YANSHOU_NOTIFY_ID;
        } else if (type.equals(Constant.TYPE_SYSTEM_MSG)) {
            notifyId = Constant.SYSTEM_NOTIFY_ID;
        } else if (type.equals(Constant.TYPE_PLAN_COMMENT_MSG)) {
            notifyId = Constant.PLAN_COMMENT_NOTIFY_ID;
        } else if (type.equals(Constant.TYPE_SECTION_COMMENT_MSG)) {
            notifyId = Constant.SECTION_COMMENT_NOTIFY_ID;
        } else if (type.equals(Constant.TYPE_DESIGNER_RESPONSE_MSG)) {
            notifyId = Constant.DESIGNER_RESPONSE_NOTIFY_ID;
        } else if (type.equals(Constant.TYPE_DESIGNER_REJECT_MSG)) {
            notifyId = Constant.DESIGNER_REJECT_NOTIFY_ID;
        } else if (type.equals(Constant.TYPE_DESIGNER_UPLOAD_PLAN_MSG)) {
            notifyId = Constant.DESIGNER_UPLOAD_PLAN_NOTIFY_ID;
        } else if (type.equals(Constant.TYPE_DESIGNER_CONFIG_CONTRACT_MSG)) {
            notifyId = Constant.DESIGNER_CONFIG_CONTRACT_NOTIFY_ID;
        } else if (type.equals(Constant.TYPE_DESIGNER_REJECT_DELAY_MSG)) {
            notifyId = Constant.DESIGNER_REJECT_DELAY_NOTIFY_ID;
        } else if (type.equals(Constant.TYPE_DESIGNER_AGREE_DELAY_MSG)) {
            notifyId = Constant.DESIGNER_AGREE_DELAY_NOTIFY_ID;
        }
        Intent mainIntent = new Intent(context, MainActivity.class);
        Intent checkIntent;
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (message.getType().equals(Constant.TYPE_SECTION_COMMENT_MSG) || message.getType().equals(Constant.TYPE_PLAN_COMMENT_MSG)) {
            checkIntent = new Intent(context, CommentListActivity_.class);
        } else {
            checkIntent = new Intent(context, NoticeActivity.class);
        }
        Intent[] intents = {mainIntent, checkIntent};
        pendingIntent = PendingIntent.getActivities(context, 0, intents,
                PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setTicker(context.getResources().getText(R.string.app_name));
        mRemoteViews.setTextViewText(R.id.list_item_title, context.getResources().getText(R.string.app_name));
        mRemoteViews.setTextViewText(R.id.list_item_date, DateFormatTool.toLocalTimeString(message.getTime()));
        mRemoteViews.setTextViewText(R.id.list_item_content, message.getContent());
        builder.setContent(mRemoteViews);
        builder.setWhen(System.currentTimeMillis());
        builder.setAutoCancel(true);
        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        notification.vibrate = new long[]{0, 300, 500, 700};
        notification.sound = Uri.parse("android.resource://"
                + context.getPackageName() + "/" + R.raw.message);
        nManager.notify(notifyId, notification);
    }

}

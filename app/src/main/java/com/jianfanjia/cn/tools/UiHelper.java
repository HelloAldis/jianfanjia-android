package com.jianfanjia.cn.tools;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RemoteViews;

import com.jianfanjia.cn.activity.CheckActivity;
import com.jianfanjia.cn.activity.MainActivity;
import com.jianfanjia.cn.activity.NotifyActivity;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.bean.NotifyMessage;
import com.jianfanjia.cn.cache.DataManagerNew;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.service.UpdateService;

import java.io.File;

public class UiHelper {

    public static void sendNotifycation(Context context, NotifyMessage message) {
        int notifyId = -1;
        NotificationManager nManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                context);
        RemoteViews mRemoteViews = new RemoteViews(context.getPackageName(), R.layout.view_custom_notify);
        mRemoteViews.setImageViewResource(R.id.list_item_img, R.mipmap.icon_logo);
        builder.setSmallIcon(R.mipmap.icon_notify);
        String type = message.getType();
        PendingIntent pendingIntent = null;
        if (type.equals(Constant.YANQI_NOTIFY)) {
            notifyId = Constant.YANQI_NOTIFY_ID;
            builder.setTicker(context.getResources()
                    .getText(R.string.yanqiText));
            mRemoteViews.setTextViewText(R.id.list_item_title, context.getResources()
                    .getText(R.string.yanqiText));
            mRemoteViews.setTextViewText(R.id.list_item_date, DateFormatTool.toLocalTimeString(message.getTime()));
            mRemoteViews.setTextViewText(R.id.list_item_content, message.getContent());
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
            builder.setTicker(context.getResources()
                    .getText(R.string.fukuanText));
            mRemoteViews.setTextViewText(R.id.list_item_title, context.getResources()
                    .getText(R.string.fukuanText));
            mRemoteViews.setTextViewText(R.id.list_item_date, DateFormatTool.toLocalTimeString(message.getTime()));
            mRemoteViews.setTextViewText(R.id.list_item_content, message.getContent());
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
            builder.setTicker(context.getResources()
                    .getText(R.string.caigouText));
            mRemoteViews.setTextViewText(R.id.list_item_title, context.getResources()
                    .getText(R.string.caigouText));
            mRemoteViews.setTextViewText(R.id.list_item_date, DateFormatTool.toLocalTimeString(message.getTime()));
            mRemoteViews.setTextViewText(R.id.list_item_content, message.getContent());
            Intent mainIntent = new Intent(context, MainActivity.class);
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            Intent notifyIntent = new Intent(context, NotifyActivity.class);
            notifyIntent.putExtra("Type", type);
            Intent[] intents = {mainIntent, notifyIntent};
            pendingIntent = PendingIntent.getActivities(context, 0, intents,
                    PendingIntent.FLAG_UPDATE_CURRENT);
        } else if (type.equals(Constant.CONFIRM_CHECK_NOTIFY)) {
            notifyId = Constant.YANSHOU_NOTIFY_ID;
            builder.setTicker(context.getResources().getText(
                    R.string.yanshouText));
            mRemoteViews.setTextViewText(R.id.list_item_title, context.getResources()
                    .getText(R.string.yanshouText));
            mRemoteViews.setTextViewText(R.id.list_item_date, DateFormatTool.toLocalTimeString(message.getTime()));
            mRemoteViews.setTextViewText(R.id.list_item_content, message.getContent());
            Intent mainIntent = new Intent(context, MainActivity.class);
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            Intent checkIntent = new Intent(context, CheckActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(Constant.PROCESS_NAME, message.getSection());
            bundle.putString(Constant.PROCESS_STATUS, message.getStatus());
            bundle.putString(Global.PROCESS_ID, message.getProcessid());
            checkIntent.putExtras(bundle);
            Intent[] intents = {mainIntent, checkIntent};
            pendingIntent = PendingIntent.getActivities(context, 0, intents,
                    PendingIntent.FLAG_UPDATE_CURRENT);
        }
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

    /**
     * 检查新版本
     *
     * @param context
     * @param listener
     */
    public static void checkNewVersion(Context context, ApiUiUpdateListener listener) {
        JianFanJiaClient.checkVersion(context, listener, context);
    }


    /**
     * 开启更新服务
     *
     * @param context
     * @param download_url
     */
    public static void startUpdateService(Context context, String download_url) {
        if (download_url == null)
            return;
        Intent intent = new Intent(context, UpdateService.class);
        intent.putExtra(Constant.DOWNLOAD_URL, download_url);
        context.startService(intent);
    }

    /**
     * 发送需求页面更新广播
     *
     * @param context
     */
    public static void sendUpdateBroast(Context context) {
        Intent intent = new Intent();
        intent.setAction(Global.ACTION_UPDATE);
        context.sendBroadcast(intent);
    }

    /**
     * @param root         最外层布局，需要调整的布局
     * @param scrollToView 被键盘遮挡的scrollToView，滚动root,使scrollToView在root可视区域的底部
     */
    public static void controlKeyboardLayout(final View root, final View scrollToView) {
        root.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        Rect rect = new Rect();
                        // 获取root在窗体的可视区域
                        root.getWindowVisibleDisplayFrame(rect);
                        // 获取root在窗体的不可视区域高度(被其他View遮挡的区域高度)
                        int rootInvisibleHeight = root.getRootView()
                                .getHeight() - rect.bottom;
                        // 若不可视区域高度大于100，则键盘显示
                        if (rootInvisibleHeight > 100) {
                            int[] location = new int[2];
                            // 获取scrollToView在窗体的坐标
                            scrollToView.getLocationInWindow(location);
                            // 计算root滚动高度，使scrollToView在可见区域
                            int srollHeight = (location[1] + scrollToView
                                    .getHeight()) - rect.bottom;
                            root.scrollTo(0, srollHeight);
                        } else {
                            // 键盘隐藏
                            root.scrollTo(0, 0);
                        }
                    }
                });
    }

    public static void intentTo(Context context, Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(context, clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }

    /**
     * 动态计算listview 的高度
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount()) - 1);
//        listView.getDividerHeight();//获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    /**
     * 拍照
     *
     * @return
     */
    public static Intent createShotIntent(File tempFile) {
        if (isCameraCanUse()) {
            DataManagerNew.getInstance().setPicPath(tempFile.getAbsolutePath());
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Uri uri = Uri.fromFile(tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            return intent;
        } else {
            return null;
        }
    }

    public static boolean isCameraCanUse() {
        boolean canUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open();
            LogTool.d("camera", "can open");
        } catch (Exception e) {
            LogTool.d("camera", "can't open");
            canUse = false;
        }
        if (canUse) {
            if (null != mCamera) {
                mCamera.release();
                mCamera = null;
            }
        }
        return canUse;
    }

}

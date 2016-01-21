package com.jianfanjia.cn.designer.tools;

import android.animation.Animator;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
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
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RemoteViews;

import com.jianfanjia.cn.designer.activity.MainActivity;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.activity.my.NotifyActivity;
import com.jianfanjia.cn.designer.activity.requirement.CheckActivity;
import com.jianfanjia.cn.designer.bean.NotifyMessage;
import com.jianfanjia.cn.designer.cache.DataManagerNew;
import com.jianfanjia.cn.designer.config.Constant;
import com.jianfanjia.cn.designer.config.Global;
import com.jianfanjia.cn.designer.http.JianFanJiaClient;
import com.jianfanjia.cn.designer.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.designer.service.UpdateService;

import java.io.File;

public class UiHelper {

    public static void IntentToPhone(Context context,String phone){
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 设置listview下落动画
     * @param context
     * @param viewGroup
     */
    public static void setLayoutAnim(Context context,ViewGroup viewGroup){
        Animation animation= AnimationUtils.loadAnimation(context, R.anim.listview_from_top);

        //得到一个LayoutAnimationController对象；

        LayoutAnimationController lac = new LayoutAnimationController(animation);

        //设置控件显示的顺序；

        lac.setOrder(LayoutAnimationController.ORDER_NORMAL);

        //设置控件显示间隔时间；

        lac.setDelay(0.1f);

        //为ListView设置LayoutAnimationController属性；

        viewGroup.setLayoutAnimation(lac);
    }

    /**
     * 对按钮进行做点击效果的
     * @param view
     * @param listener
     */
    public static void imageButtonAnim(View view,Animator.AnimatorListener listener)
    {
        Keyframe kf0 = Keyframe.ofFloat(0f, 1.0f);
        Keyframe kf1 = Keyframe.ofFloat(0.17f, 1.4f);
        Keyframe kf2 = Keyframe.ofFloat(0.34f, 0.9f);
        Keyframe kf3 = Keyframe.ofFloat(0.51f, 1.15f);
        Keyframe kf4 = Keyframe.ofFloat(0.68f, 0.95f);
        Keyframe kf5 = Keyframe.ofFloat(0.85f, 1.02f);
        Keyframe kf6 = Keyframe.ofFloat(1.0f, 1.0f);
        PropertyValuesHolder propertyValuesHolderScaleXHolder = PropertyValuesHolder.ofKeyframe("scaleX", kf0, kf1,
                kf2, kf3, kf4, kf5, kf6);
        PropertyValuesHolder propertyValuesHolderScaleYHolder = PropertyValuesHolder.ofKeyframe("scaleY", kf0, kf1,
                kf2, kf3, kf4, kf5, kf6);
        ObjectAnimator rotaAnimator = ObjectAnimator.ofPropertyValuesHolder(view,
                propertyValuesHolderScaleXHolder,propertyValuesHolderScaleYHolder);
        rotaAnimator.setDuration(500);
        rotaAnimator.setInterpolator(new AccelerateInterpolator());
        if(listener != null){
            rotaAnimator.addListener(listener);
        }
        rotaAnimator.start();
    }

    public static void sendNotifycation(Context context, NotifyMessage message) {
        int notifyId = -1;
        NotificationManager nManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                context);
        RemoteViews mRemoteViews = new RemoteViews(context.getPackageName(), R.layout.view_custom_notify);
        mRemoteViews.setImageViewResource(R.id.list_item_img, R.drawable.ic_launcher);
        builder.setSmallIcon(R.mipmap.icon_notify);
        String type = message.getType();
        LogTool.d("sendNotifycation","type =" + type);
        PendingIntent pendingIntent = null;
        if (type.equals(Constant.YANQI_NOTIFY)) {
            LogTool.d("sendNotifycation",context.getResources()
                    .getString(R.string.yanqiText));
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
            builder.setTicker(context.getResources().getText(R.string.yanshouText));
            mRemoteViews.setTextViewText(R.id.list_item_title, context.getResources().getText(R.string.yanshouText));
            mRemoteViews.setTextViewText(R.id.list_item_date, DateFormatTool.toLocalTimeString(message.getTime()));
            mRemoteViews.setTextViewText(R.id.list_item_content, message.getContent());
            Intent mainIntent = new Intent(context, MainActivity.class);
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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

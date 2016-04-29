package com.jianfanjia.cn.tools;

import android.animation.Animator;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.jianfanjia.cn.supervisor.AppManager;
import com.jianfanjia.cn.supervisor.R;
import com.jianfanjia.cn.supervisor.activity.LoginNewActivity;
import com.jianfanjia.cn.supervisor.activity.common.WebViewActivity;
import com.jianfanjia.cn.supervisor.application.MyApplication;
import com.jianfanjia.cn.supervisor.business.DataManagerNew;
import com.jianfanjia.cn.supervisor.config.Constant;
import com.jianfanjia.cn.supervisor.config.Global;
import com.jianfanjia.cn.supervisor.config.Url_New;
import com.jianfanjia.cn.supervisor.service.UpdateService;
import com.jianfanjia.cn.view.baseview.HorizontalDividerDecoration;
import com.jianfanjia.common.tool.FileUtil;
import com.jianfanjia.common.tool.LogTool;
import com.jianfanjia.common.tool.TDevice;
import com.nostra13.universalimageloader.core.ImageLoader;

public class UiHelper {

    /**
     * 实现文本复制功能
     * add by wangqianzhou
     *
     * @param content
     */
    public static void copy(String content, Context context) {
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
        showShortToast(context.getString(R.string.follow_weixin_success));
    }

    public static void intentToPackget365Detail(Context context) {
        Bundle bundle = new Bundle();
        bundle.putString(Global.WEB_VIEW_URL, Url_New.getInstance().PACKGET365_DETAIL_URL);
        IntentUtil.startActivity(context, WebViewActivity.class, bundle);
    }


    /**
     * 生成一个默认的分割线
     *
     * @param context
     * @return
     */
    public static HorizontalDividerDecoration buildDefaultHeightDecoration(Context context) {
        return new HorizontalDividerDecoration(TDevice.dip2px(context, 10));
    }

    /**
     * 计算缓存大小
     *
     * @return
     */
    public static String caculateCacheSize() {
        long fileSize = 0;
        String cacheSize = "0KB";
        File filesDir = ImageLoader.getInstance().getDiskCache().getDirectory();
        fileSize += FileUtil.getDirSize(filesDir);

        // 2.2版本才有将应用缓存转移到sd卡的功能
        if (MyApplication.isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)) {
            File externalCacheDir = MyApplication.getInstance().getExternalCacheDir();
            fileSize += FileUtil.getDirSize(externalCacheDir);
        }
        if (fileSize > 0) {
            cacheSize = FileUtil.formatFileSize(fileSize);
        }
        return cacheSize;
    }

    /**
     * 跳转到登录界面
     */
    public static void forbiddenToLogin() {
        Intent intent = new Intent(MyApplication.getInstance(), LoginNewActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MyApplication.getInstance().startActivity(intent);
        AppManager.getAppManager().finishAllActivity();
        DataManagerNew.getInstance().cleanData();
        showShortToast("登录过期，请重新登录！");
    }

    /**
     * 显示toast
     *
     * @param text
     */
    public static void showShortToast(String text) {
        Toast.makeText(MyApplication.getInstance(), text, Toast.LENGTH_SHORT).show();
    }

    /**
     * 调整FrameLayout大小
     *
     * @param tp
     */
    public static void resizePikcer(FrameLayout tp) {
        List<NumberPicker> npList = findNumberPicker(tp);
        for (NumberPicker np : npList) {
            resizeNumberPicker(np);
        }
    }

    /**
     * 得到viewGroup里面的numberpicker组件
     *
     * @param viewGroup
     * @return
     */
    public static List<NumberPicker> findNumberPicker(ViewGroup viewGroup) {
        List<NumberPicker> npList = new ArrayList<>();
        View child = null;
        if (null != viewGroup) {
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                child = viewGroup.getChildAt(i);
                if (child instanceof NumberPicker) {
                    npList.add((NumberPicker) child);
                } else if (child instanceof LinearLayout) {
                    List<NumberPicker> result = findNumberPicker((ViewGroup) child);
                    if (result.size() > 0) {
                        return result;
                    }
                }
            }
        }
        return npList;
    }

    /*
     * 调整numberpicker大小
     */
    public static void resizeNumberPicker(NumberPicker np) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 0, 10, 0);
        np.setLayoutParams(params);

        np.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        setNumberPickerDividerColor(np);
    }

    /**
     * 反射设置numberPicker属性
     *
     * @param numberPicker
     */
    public static void setNumberPickerDividerColor(NumberPicker numberPicker) {
        NumberPicker picker = numberPicker;
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (Field pf : pickerFields) {
            if (pf.getName().equals("mInputText")) {
            }
        }
    }

    public static void callPhoneIntent(Context context, String phone) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 设置listview下落动画
     *
     * @param context
     * @param viewGroup
     */
    public static void setLayoutAnim(Context context, ViewGroup viewGroup) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.listview_from_top);

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
     *
     * @param view
     * @param listener
     */
    public static void imageButtonAnim(View view, Animator.AnimatorListener listener) {
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
                propertyValuesHolderScaleXHolder, propertyValuesHolderScaleYHolder);
        rotaAnimator.setDuration(500);
        rotaAnimator.setInterpolator(new AccelerateInterpolator());
        if (listener != null) {
            rotaAnimator.addListener(listener);
        }
        rotaAnimator.start();
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

package com.jianfanjia.cn.view.guideview;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.jianfanjia.common.tool.LogTool;
import com.jianfanjia.common.tool.TDevice;

/**
 * Description: com.jianfanjia.cn.view.guideview
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-07-22 14:49
 */
public class GuideViewManager {

    private Context mContext;
    private GestureGuideView mGestureGuideView;
    private WindowManager mWindowManager;
    private View.OnClickListener mOnClickListener;

    public GuideViewManager(Context context, View.OnClickListener onClickListener) {
        this.mContext = context;
        this.mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        this.mOnClickListener = onClickListener;
    }

    public void showGuideView(int posX, int posY, int radius) {
        // 动态初始化图层
        mGestureGuideView = new GestureGuideView(mContext);
        mGestureGuideView.setCicrePosition(posX, posY, radius);
        mGestureGuideView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        mGestureGuideView.setOnClickListener(mOnClickListener);
        // 设置LayoutParams参数
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        // 设置显示的类型，TYPE_PHONE指的是来电话的时候会被覆盖，其他时候会在最前端，显示位置在stateBar下面，其他更多的值请查阅文档
        params.type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL;
        // 设置显示格式
        params.format = PixelFormat.RGBA_8888;
        // 设置对齐方式
        params.gravity = Gravity.LEFT | Gravity.TOP;
        // 设置宽高
        params.width = (int) TDevice.getScreenWidth();
        params.height = (int) TDevice.getScreenHeight();

        // 添加到当前的窗口上

        mWindowManager.addView(mGestureGuideView, params);
    }

    public void hideGuideView() {
        if (mWindowManager != null) {
            LogTool.d("remove mGuideView");
            mWindowManager.removeView(mGestureGuideView);
        }
    }
}

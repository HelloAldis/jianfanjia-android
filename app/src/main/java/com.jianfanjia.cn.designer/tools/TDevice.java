package com.jianfanjia.cn.designer.tools;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.jianfanjia.cn.designer.application.MyApplication;

public class TDevice {

    public static DisplayMetrics getDisplayMetrics() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((WindowManager) MyApplication.getInstance().getSystemService(
                Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(
                displaymetrics);
        return displaymetrics;
    }

    public static float getScreenHeight() {
        return getDisplayMetrics().heightPixels;
    }

    public static float getScreenWidth() {
        return getDisplayMetrics().widthPixels;
    }

}

package com.jianfanjia.cn.tools;

import android.content.Context;

/**
 * Name: ScreenUtil
 * User: fengliang
 * Date: 2015-10-14
 * Time: 17:19
 */
public class ScreenUtil {
    /**
     * 用于获取状态栏的高度。 使用Resource对象获取
     *
     * @return 返回状态栏高度的像素值。
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}  

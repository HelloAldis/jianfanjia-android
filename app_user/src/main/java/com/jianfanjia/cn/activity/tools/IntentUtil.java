package com.jianfanjia.cn.activity.tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Description: com.jianfanjia.cn.tools
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-03-03 10:14
 */
public class IntentUtil {

    private static final int NoFrag = -1;

    public static void startActivity(Context context, Class<?> clazz, Bundle bundle) {
        startActivityHasFlag(context, clazz, bundle, NoFrag);
    }

    public static void startActivity(Context context, Class<?> clazz) {
        startActivity(context, clazz, null);
    }

    // 通过Action跳转界面
    public static void startActivity(Context context, String action) {
        startActivity(context, action, null);
    }

    // 含有Bundle通过Action跳转界面
    public static void startActivity(Context context, String action, Bundle bundle) {
        startActivityHasFlag(context, action, bundle, NoFrag);
    }

    public static void startActivityHasFlag(Context context, String action, Bundle bundle, int flag) {
        Intent intent = new Intent();
        intent.setAction(action);
        if (flag > 0) {
            intent.setFlags(flag);
        }
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }

    public static void startActivityHasFlag(Context context, Class<?> clazz, Bundle bundle, int flag) {
        Intent intent = new Intent(context, clazz);
        if (flag > 0) {
            intent.setFlags(flag);
        }
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }

    public static void startActivityForResult(Activity activity, Class<?> clazz, int requestCode) {
        startActivityForResult(activity, clazz, null, requestCode);
    }

    public static void startActivityForResult(Activity activity, Class<?> clazz, Bundle bundle, int requestCode) {
        if (requestCode < 0) {
            throw new IllegalArgumentException("the requestCode must be biger than 0");
        }
        Intent intent = new Intent(activity.getApplicationContext(), clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        activity.startActivityForResult(intent, requestCode);
    }

    public static void startActivityForResult(Fragment fragment, Class<?> clazz, int requestCode) {
        startActivityForResult(fragment, clazz, null, requestCode);
    }

    public static void startActivityForResult(Fragment fragment, Class<?> clazz, Bundle bundle, int requestCode) {
        if (requestCode < 0) {
            throw new IllegalArgumentException("the requestCode must be biger than 0");
        }
        Intent intent = new Intent(fragment.getContext(), clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        fragment.startActivityForResult(intent, requestCode);
    }


}

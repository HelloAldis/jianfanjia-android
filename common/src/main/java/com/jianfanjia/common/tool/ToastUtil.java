package com.jianfanjia.common.tool;

import android.widget.Toast;

import com.jianfanjia.common.base.application.BaseApplication;

/**
 * Created by Aldis on 16/3/24.
 */
public class ToastUtil {

    public static void showShortTost(String text) {
        Toast.makeText(BaseApplication.getInstance().getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    public static void showLongTost(String text) {
        Toast.makeText(BaseApplication.getInstance().getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }
}

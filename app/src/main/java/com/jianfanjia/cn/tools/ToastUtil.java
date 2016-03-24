package com.jianfanjia.cn.tools;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by Aldis on 16/3/24.
 */
public class ToastUtil {
    public void showShortTost(Activity activity, String text) {
        Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
    }

    public void showLongTost(Activity activity, String text) {
        Toast.makeText(activity, text, Toast.LENGTH_LONG).show();
    }
}

package com.jianfanjia.common.tool;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Aldis on 16/3/24.
 */
public class ToastUtil {

    public static void showShortTost(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void showLongTost(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }
}

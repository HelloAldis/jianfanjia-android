package com.aldis.hud;

import android.content.Context;

/**
 * Created by Aldis on 16/3/25.
 */
public class Util {
    private static float scale;

    public static int dp2Pixel(float dp, Context context) {
        if (scale == 0) {
            scale = context.getResources().getDisplayMetrics().density;
        }
        return (int) (dp * scale);
    }

}

package com.aldis.hud;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by Aldis on 16/3/25.
 */
public class DialogLayout extends LinearLayout {

    public DialogLayout(Context context) {
        super(context);
    }

    public DialogLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void initLayoutUI(int color, float cornerRadius) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setColor(color);
        drawable.setCornerRadius(cornerRadius);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(drawable);
        } else {
            //noinspection deprecation
            setBackgroundDrawable(drawable);
        }
    }
}

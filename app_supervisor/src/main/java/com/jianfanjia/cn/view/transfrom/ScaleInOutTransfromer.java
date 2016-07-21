package com.jianfanjia.cn.view.transfrom;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

/**
 * Description: com.jianfanjia.cn.view.viewpager.transfrom
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-05-03 15:29
 */
public class ScaleInOutTransfromer implements ViewPager.PageTransformer{

    private static final float MIN_SCALE = 0.85f;
    private static final float MIN_ALPHA = 0.6f;

    @Override
    public void transformPage(View view, float position) {
        Log.d(this.getClass().getName(), "position =" + position);

        int pageWidth = view.getWidth();
        int pageHeight = view.getHeight();

        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.setAlpha(MIN_ALPHA);
        } else if (position <= 1.0) { // [-1,1]
            // Modify the default slide transition to
            // shrink the page as well
            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
            float vertMargin = pageHeight * (1 - scaleFactor) / 2;
            float horzMargin = pageWidth * (1 - scaleFactor) / 2;
            if (position < 0) {
                view.setTranslationX(horzMargin - vertMargin / 2);
            } else {
                view.setTranslationX(-horzMargin + vertMargin / 2);
            }
            // Scale the page down (between MIN_SCALE and 1)
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);
            // Fade the page relative to its size.
            view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE)
                    / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            view.setAlpha(MIN_ALPHA);
        }
    }
}
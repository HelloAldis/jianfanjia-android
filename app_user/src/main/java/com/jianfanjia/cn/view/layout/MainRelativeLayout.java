package com.jianfanjia.cn.view.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.Scroller;

/**
 * Description: com.jianfanjia.cn.view.layout
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-05-04 12:10
 */
public class MainRelativeLayout extends RelativeLayout {

    private Scroller mScroller;



    public MainRelativeLayout(Context context) {
        super(context);
    }

    public MainRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}

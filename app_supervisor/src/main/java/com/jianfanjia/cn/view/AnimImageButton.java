package com.jianfanjia.cn.view;

import android.animation.Animator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;

import com.jianfanjia.cn.tools.UiHelper;


/**
 * Description: com.jianfanjia.cn.view
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-12-24 10:16
 */
public class AnimImageButton extends ImageButton {

    public AnimImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 点击的时候执行动画
     *
     * @param l
     * @param listener
     */
    public void setOnClickListener(final OnClickListener l, final Animator.AnimatorListener listener) {
        OnClickListener wrapClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (l != null) {
                    l.onClick(AnimImageButton.this);
                }
                onClickAnim(listener);
            }
        };
        setOnClickListener(wrapClickListener);
    }

    protected void onClickAnim(Animator.AnimatorListener listener) {
        UiHelper.imageButtonAnim(AnimImageButton.this, listener);
    }
}

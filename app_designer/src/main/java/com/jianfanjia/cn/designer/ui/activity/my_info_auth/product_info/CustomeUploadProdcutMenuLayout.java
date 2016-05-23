package com.jianfanjia.cn.designer.ui.activity.my_info_auth.product_info;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.jianfanjia.cn.designer.R;
import com.jianfanjia.common.tool.TDevice;

/**
 * Description: com.jianfanjia.cn.designer.ui.activity.my_info_auth.product_info
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-05-23 10:48
 */
public class CustomeUploadProdcutMenuLayout extends FrameLayout {

    private static final int every_indinvation = 24;
    private static final int ANI_DURATION = 200;

    private boolean isOpen = false;//是否展开
    private boolean isAni = false;//是否正在动画；
    private ImageView openOrCloseView;
    private ImageView clearView;
    private ImageView editView;
    private ImageView settingCoverView;
    private int clearTranslitionX;
    private int editTranslitionX;
    private int settingCoverTranslitionX;


    public CustomeUploadProdcutMenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        View view = inflate(context, R.layout.custome_view_upload_menu, null);
        addView(view);

        openOrCloseView = (ImageView) findViewById(R.id.btn_openOrClose);
        clearView = (ImageView) findViewById(R.id.btn_clear);
        editView = (ImageView) findViewById(R.id.btn_edit);
        settingCoverView = (ImageView) findViewById(R.id.btn_setting_cover);
        openOrCloseView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isAni) {
                    if (isOpen) {
                        closeMenu();
                    } else {
                        openMenu();
                    }
                }
            }
        });

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        settingCoverTranslitionX = (openOrCloseView.getMeasuredWidth() + TDevice.dip2px(getContext(), every_indinvation)) * 3;
        editTranslitionX = (openOrCloseView.getMeasuredWidth() + TDevice.dip2px(getContext(), every_indinvation)) * 2;
        clearTranslitionX = (openOrCloseView.getMeasuredWidth() + TDevice.dip2px(getContext(), every_indinvation)) * 1;
    }

    private void openMenu() {
        ViewPropertyAnimator openOrClosetranslationAni = openOrCloseView.animate().rotation(-45).setDuration(100);
        final ValueAnimator settingCovertranslationAni = ValueAnimator.ofInt(0, settingCoverTranslitionX);
        settingCovertranslationAni.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int x = (Integer) animation.getAnimatedValue();
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) settingCoverView.getLayoutParams();
                lp.rightMargin = TDevice.dip2px(getContext(), every_indinvation) + x;
                settingCoverView.setLayoutParams(lp);
            }
        });
        final ValueAnimator edittranslationAni = ValueAnimator.ofInt(0, editTranslitionX);
        edittranslationAni.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int x = (Integer) animation.getAnimatedValue();
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) editView.getLayoutParams();
                lp.rightMargin = TDevice.dip2px(getContext(), every_indinvation) + x;
                editView.setLayoutParams(lp);
            }
        });
        final ValueAnimator cleartranslationAni = ValueAnimator.ofInt(0, clearTranslitionX);
        cleartranslationAni.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int x = (Integer) animation.getAnimatedValue();
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) clearView.getLayoutParams();
                lp.rightMargin = TDevice.dip2px(getContext(), every_indinvation) + x;
                clearView.setLayoutParams(lp);
            }
        });
        openOrClosetranslationAni.start();
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(ANI_DURATION);
        animatorSet.playTogether(settingCovertranslationAni, cleartranslationAni, edittranslationAni);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isAni = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isOpen = true;
                isAni = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();
    }

    private void closeMenu() {
        ViewPropertyAnimator openOrClosetranslationAni = openOrCloseView.animate().rotation(0).setDuration(100);
        final ValueAnimator settingCovertranslationAni = ValueAnimator.ofInt(settingCoverTranslitionX, 0);
        settingCovertranslationAni.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int x = (Integer) animation.getAnimatedValue();
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) settingCoverView.getLayoutParams();
                lp.rightMargin = TDevice.dip2px(getContext(), every_indinvation) + x;
                settingCoverView.setLayoutParams(lp);
            }
        });
        final ValueAnimator edittranslationAni = ValueAnimator.ofInt(editTranslitionX, 0);
        edittranslationAni.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int x = (Integer) animation.getAnimatedValue();
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) editView.getLayoutParams();
                lp.rightMargin = TDevice.dip2px(getContext(), every_indinvation) + x;
                editView.setLayoutParams(lp);
            }
        });
        final ValueAnimator cleartranslationAni = ValueAnimator.ofInt(clearTranslitionX, 0);
        cleartranslationAni.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int x = (Integer) animation.getAnimatedValue();
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) clearView.getLayoutParams();
                lp.rightMargin = TDevice.dip2px(getContext(), every_indinvation) + x;
                clearView.setLayoutParams(lp);
            }
        });
        openOrClosetranslationAni.start();
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(ANI_DURATION);
        animatorSet.playTogether(settingCovertranslationAni, cleartranslationAni, edittranslationAni);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isAni = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isOpen = false;
                isAni = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();

    }

    public void setClearClickListerer(OnClickListener listerer) {
        clearView.setOnClickListener(listerer);
    }

    public void setSettingCoverListener(OnClickListener listener) {
        settingCoverView.setOnClickListener(listener);
    }

    public void setEditListener(OnClickListener listener) {
        editView.setOnClickListener(listener);
    }


}

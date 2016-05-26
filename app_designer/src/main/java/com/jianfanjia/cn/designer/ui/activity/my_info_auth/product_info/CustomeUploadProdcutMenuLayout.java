package com.jianfanjia.cn.designer.ui.activity.my_info_auth.product_info;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.jianfanjia.cn.designer.R;
import com.jianfanjia.common.tool.LogTool;
import com.jianfanjia.common.tool.TDevice;

/**
 * Description: com.jianfanjia.cn.designer.ui.activity.my_info_auth.product_info
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-05-23 10:48
 */
public class CustomeUploadProdcutMenuLayout extends FrameLayout {

    private static final int every_indinvation = 28;
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

    private FrameLayout.LayoutParams initLayoutParams;
    private FrameLayout.LayoutParams clearLayoutParams;
    private FrameLayout.LayoutParams editLayoutParams;
    private FrameLayout.LayoutParams settingCoverLayoutParams;

    private int initRightMaigin;

    public CustomeUploadProdcutMenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        View view = inflate(context, R.layout.custome_view_upload_menu, null);
        addView(view);

        openOrCloseView = (ImageView) findViewById(R.id.btn_openOrClose);
        clearView = (ImageView) findViewById(R.id.btn_clear);
        editView = (ImageView) findViewById(R.id.btn_edit);
        settingCoverView = (ImageView) findViewById(R.id.btn_setting_cover);

        initRightMaigin = TDevice.dip2px(context, every_indinvation);
        initMoveDistance();
        setOpenStatus();
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

    public void setOpenStatus() {

        initLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup
                .LayoutParams.WRAP_CONTENT);
        initLayoutParams.gravity = Gravity.RIGHT;
        initLayoutParams.rightMargin = initRightMaigin;

        openOrCloseView.setLayoutParams(initLayoutParams);
        clearView.setLayoutParams(initLayoutParams);
        editView.setLayoutParams(initLayoutParams);
        settingCoverView.setLayoutParams(initLayoutParams);

        settingCoverView.setVisibility(INVISIBLE);
        clearView.setVisibility(INVISIBLE);
        editView.setVisibility(INVISIBLE);
        isOpen = false;
        openOrCloseView.setRotation(45);

    }

    public void setCloseStatus() {
        initLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup
                .LayoutParams.WRAP_CONTENT);
        initLayoutParams.gravity = Gravity.RIGHT;
        initLayoutParams.rightMargin = initRightMaigin;

        openOrCloseView.setLayoutParams(initLayoutParams);

        clearLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup
                .LayoutParams.WRAP_CONTENT);
        clearLayoutParams.gravity = Gravity.RIGHT;
        clearLayoutParams.rightMargin = clearTranslitionX;
        clearView.setLayoutParams(clearLayoutParams);

        editLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup
                .LayoutParams.WRAP_CONTENT);
        editLayoutParams.gravity = Gravity.RIGHT;
        editLayoutParams.rightMargin = editTranslitionX;
        editView.setLayoutParams(editLayoutParams);

        settingCoverLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup
                .LayoutParams.WRAP_CONTENT);
        settingCoverLayoutParams.gravity = Gravity.RIGHT;
        settingCoverLayoutParams.rightMargin = settingCoverTranslitionX;
        settingCoverView.setLayoutParams(settingCoverLayoutParams);

        settingCoverView.setVisibility(VISIBLE);
        clearView.setVisibility(VISIBLE);
        editView.setVisibility(VISIBLE);
        isOpen = true;
        openOrCloseView.setRotation(0);
    }

    private void initMoveDistance() {
        LogTool.d(this.getClass().getName(), "drawable width =" + openOrCloseView.getDrawable().getIntrinsicWidth());
        clearTranslitionX = 2 * initRightMaigin;
        editTranslitionX = clearTranslitionX * 2;
        settingCoverTranslitionX = clearTranslitionX * 3;
    }

    private void openMenu() {
        ViewPropertyAnimator openOrClosetranslationAni = openOrCloseView.animate().rotation(0).setDuration(100);
        final ValueAnimator settingCovertranslationAni = ValueAnimator.ofInt(0, settingCoverTranslitionX);
        settingCovertranslationAni.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int x = (Integer) animation.getAnimatedValue();
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.gravity = Gravity.RIGHT;
                lp.rightMargin = initRightMaigin + x;
                settingCoverView.setLayoutParams(lp);
            }
        });
        final ValueAnimator setingCoverAlpha = ValueAnimator.ofFloat(0F, 1.0F);
        setingCoverAlpha.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                settingCoverView.setAlpha((float) animation.getAnimatedValue());
            }
        });
        final ValueAnimator edittranslationAni = ValueAnimator.ofInt(0, editTranslitionX);
        edittranslationAni.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int x = (Integer) animation.getAnimatedValue();
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup
                                .LayoutParams.WRAP_CONTENT);
                lp.gravity = Gravity.RIGHT;
                lp.rightMargin = initRightMaigin + x;
                editView.setLayoutParams(lp);
            }
        });
        final ValueAnimator editCoverAlpha = ValueAnimator.ofFloat(0F, 1.0F);
        editCoverAlpha.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                editView.setAlpha((float) animation.getAnimatedValue());
            }
        });
        final ValueAnimator cleartranslationAni = ValueAnimator.ofInt(0, clearTranslitionX);
        cleartranslationAni.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int x = (Integer) animation.getAnimatedValue();
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup
                                .LayoutParams.WRAP_CONTENT);
                lp.gravity = Gravity.RIGHT;
                lp.rightMargin = initRightMaigin + x;
                clearView.setLayoutParams(lp);
            }
        });
        final ValueAnimator clearCoverAlpha = ValueAnimator.ofFloat(0F, 1.0F);
        clearCoverAlpha.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                clearView.setAlpha((float) animation.getAnimatedValue());
            }
        });
        openOrClosetranslationAni.start();
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(ANI_DURATION);
        animatorSet.playTogether(settingCovertranslationAni, setingCoverAlpha, cleartranslationAni, clearCoverAlpha,
                edittranslationAni, editCoverAlpha);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                clearView.setVisibility(VISIBLE);
                editView.setVisibility(VISIBLE);
                settingCoverView.setVisibility(VISIBLE);
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
        ViewPropertyAnimator openOrClosetranslationAni = openOrCloseView.animate().rotation(45).setDuration(100);
        final ValueAnimator settingCovertranslationAni = ValueAnimator.ofInt(settingCoverTranslitionX, 0);
        settingCovertranslationAni.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int x = (Integer) animation.getAnimatedValue();
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup
                                .LayoutParams.WRAP_CONTENT);
                lp.gravity = Gravity.RIGHT;
                lp.rightMargin = initRightMaigin + x;
                settingCoverView.setLayoutParams(lp);
            }
        });
        final ValueAnimator setingCoverAlpha = ValueAnimator.ofFloat(1.0f, 0f);
        setingCoverAlpha.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                settingCoverView.setAlpha((float) animation.getAnimatedValue());
            }
        });
        final ValueAnimator edittranslationAni = ValueAnimator.ofInt(editTranslitionX, 0);
        edittranslationAni.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int x = (Integer) animation.getAnimatedValue();
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup
                                .LayoutParams.WRAP_CONTENT);
                lp.gravity = Gravity.RIGHT;
                lp.rightMargin = initRightMaigin + x;
                editView.setLayoutParams(lp);
            }
        });
        final ValueAnimator editCoverAlpha = ValueAnimator.ofFloat(1.0f, 0f);
        editCoverAlpha.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                editView.setAlpha((float) animation.getAnimatedValue());
            }
        });
        final ValueAnimator cleartranslationAni = ValueAnimator.ofInt(clearTranslitionX, 0);
        cleartranslationAni.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int x = (Integer) animation.getAnimatedValue();
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.gravity = Gravity.RIGHT;
                lp.rightMargin = initRightMaigin + x;
                clearView.setLayoutParams(lp);
            }
        });
        final ValueAnimator clearCoverAlpha = ValueAnimator.ofFloat(1.0f, 0f);
        clearCoverAlpha.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                clearView.setAlpha((float) animation.getAnimatedValue());
            }
        });
        openOrClosetranslationAni.start();
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(ANI_DURATION);
        animatorSet.playTogether(settingCovertranslationAni, setingCoverAlpha, cleartranslationAni, clearCoverAlpha,
                edittranslationAni, editCoverAlpha);
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
}

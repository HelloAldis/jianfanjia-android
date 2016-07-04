package com.jianfanjia.cn.ui.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;

import butterknife.Bind;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.bean.AnimationRect;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.tools.ImageShow;
import com.jianfanjia.cn.ui.activity.common.CommonShowPicActivity;
import com.jianfanjia.common.tool.LogTool;
import me.iwf.photopicker.utils.AnimationUtility;
import uk.co.senab.photoview.PhotoView;

/**
 * Description: com.jianfanjia.cn.ui.fragment
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-07-01 16:40
 */
public class CommonShowPicFragment extends BaseFragment {

    public static final String IMGAE_ID = "image_id";
    public static final String ANIMATION_IN = "animation_in";
    private static final int ANIMATION_DURATION = 300;

    private AnimationRect mAnimationRect;
    private String imageid;
    private boolean animationIn;

    @Bind(R.id.image_item)
    PhotoView mPhotoView;

    public static CommonShowPicFragment getInstance(String imageid, AnimationRect rect, boolean animationIn) {
        CommonShowPicFragment commonShowPicFragment = new CommonShowPicFragment();
        Bundle bundle = new Bundle();
        bundle.putString(IMGAE_ID, imageid);
        bundle.putParcelable(Constant.ANIMATION_RECT, rect);
        bundle.putBoolean(ANIMATION_IN, animationIn);
        commonShowPicFragment.setArguments(bundle);
        return commonShowPicFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAnimationRect = getArguments().getParcelable(Constant.ANIMATION_RECT);
        imageid = getArguments().getString(IMGAE_ID);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        boolean animationIn = getArguments().getBoolean(ANIMATION_IN);
        if (!TextUtils.isEmpty(imageid)) {
            ImageShow.getImageShow().displayScreenWidthThumnailImage(getActivity(), imageid, mPhotoView);
        } else {
            mPhotoView.setImageResource(R.mipmap.icon_default_pic);
            return;
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        if (animationIn) {
            ObjectAnimator animator = ((CommonShowPicActivity) getActivity()).showBackgroundAnimate();
            animator.start();
        } else {
            ((CommonShowPicActivity) getActivity()).showBackgroundImmediately();
        }

        if (animationIn) {

            mPhotoView.getViewTreeObserver()
                    .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                        @Override
                        public boolean onPreDraw() {

                            final Rect startBounds = new Rect(mAnimationRect.scaledBitmapRect);
                            final Rect finalBounds = AnimationUtility
                                    .getBitmapRectFromImageView(mPhotoView);

                            if (finalBounds == null) {
                                mPhotoView.getViewTreeObserver().removeOnPreDrawListener(this);
                                return true;
                            }

                            float startScale = (float) finalBounds.width() / startBounds.width();

                            if (startScale * startBounds.height() > finalBounds.height()) {
                                startScale = (float) finalBounds.height() / startBounds.height();
                            }

                            int deltaTop = startBounds.top - finalBounds.top;
                            int deltaLeft = startBounds.left - finalBounds.left;

                            mPhotoView.setPivotY(
                                    (mPhotoView.getHeight() - finalBounds.height()) / 2);
                            mPhotoView.setPivotX((mPhotoView.getWidth() - finalBounds.width()) / 2);

                            mPhotoView.setScaleX(1 / startScale);
                            mPhotoView.setScaleY(1 / startScale);

                            mPhotoView.setTranslationX(deltaLeft);
                            mPhotoView.setTranslationY(deltaTop);

                            mPhotoView.animate().translationY(0).translationX(0)
                                    .scaleY(1)
                                    .scaleX(1).setDuration(ANIMATION_DURATION)
                                    .setInterpolator(
                                            new AccelerateDecelerateInterpolator())
                                    .start();


                            mPhotoView.getViewTreeObserver().removeOnPreDrawListener(this);
                            return true;
                        }
                    });
            getArguments().putBoolean(ANIMATION_IN, false);
        }

    }

    @Override
    public int getLayoutId() {
        return R.layout.viewpager_item_show_pic;
    }

    public void animationExit(ObjectAnimator backgroundAnimator) {

        if (Math.abs(mPhotoView.getScale() - 1.0f) > 0.1f) {
            mPhotoView.setScale(1, true);
            return;
        }

        getActivity().overridePendingTransition(0, 0);
        animateClose(backgroundAnimator);
    }

    private void animateClose(ObjectAnimator backgroundAnimator) {

        if (mAnimationRect == null) {
            mPhotoView.animate().alpha(0);
            return;
        }

        final Rect startBounds = mAnimationRect.scaledBitmapRect;
        final Rect finalBounds = AnimationUtility.getBitmapRectFromImageView(mPhotoView);

        if (finalBounds == null) {
            mPhotoView.animate().alpha(0);
            return;
        }


        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            startScale = (float) startBounds.height() / finalBounds.height();
        } else {
            startScale = (float) startBounds.width() / finalBounds.width();
        }
        LogTool.d(this.getClass().getName(), "finalBounds.width() =" + finalBounds.width() + ",finalBounds.height() =" +
                " " + finalBounds.height());

        int deltaTop = startBounds.top - finalBounds.top;
        int deltaLeft = startBounds.left - finalBounds.left;

        mPhotoView.setPivotY((mPhotoView.getHeight() - finalBounds.height()) / 2);
        mPhotoView.setPivotX((mPhotoView.getWidth() - finalBounds.width()) / 2);

        mPhotoView.animate().translationX(deltaLeft).translationY(deltaTop)
                .scaleY(startScale)
                .scaleX(startScale)
                .setDuration(ANIMATION_DURATION)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .start();


        backgroundAnimator.start();


    }

}

package com.jianfanjia.cn.ui.fragment;

import android.animation.AnimatorSet;
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
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.tools.ImageShow;
import com.jianfanjia.cn.ui.activity.common.CommonShowPicActivity;
import com.jianfanjia.common.tool.LogTool;
import me.iwf.photopicker.entity.AnimationRect;
import me.iwf.photopicker.utils.AnimationUtility;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

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

        if (animationIn) {
            ObjectAnimator animator = ((CommonShowPicActivity) getActivity()).showBackgroundAnimate();
            animator.start();
        } else {
            ((CommonShowPicActivity) getActivity()).showBackgroundImmediately();
        }

        mPhotoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                getActivity().onBackPressed();
            }

            @Override
            public void onOutsidePhotoTap() {
                getActivity().onBackPressed();
            }
        });

        if (animationIn) {

            mPhotoView.getViewTreeObserver()
                    .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                        @Override
                        public boolean onPreDraw() {

                            if (mAnimationRect == null) {
                                mPhotoView.getViewTreeObserver().removeOnPreDrawListener(this);
                                return true;
                            }

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
                                            new AccelerateDecelerateInterpolator());

                            AnimatorSet animationSet = new AnimatorSet();
                            animationSet.setDuration(ANIMATION_DURATION);
                            animationSet
                                    .setInterpolator(new AccelerateDecelerateInterpolator());

                            animationSet.playTogether(ObjectAnimator.ofFloat(mPhotoView,
                                    "clipBottom",
                                    AnimationRect.getClipBottom(mAnimationRect, finalBounds), 0));
                            animationSet.playTogether(ObjectAnimator.ofFloat(mPhotoView,
                                    "clipRight",
                                    AnimationRect.getClipRight(mAnimationRect, finalBounds), 0));
                            animationSet.playTogether(ObjectAnimator.ofFloat(mPhotoView,
                                    "clipTop", AnimationRect.getClipTop(mAnimationRect, finalBounds), 0));
                            animationSet.playTogether(ObjectAnimator.ofFloat(mPhotoView,
                                    "clipLeft", AnimationRect.getClipLeft(mAnimationRect, finalBounds), 0));

                            animationSet.start();


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
            backgroundAnimator.start();
            return;
        }

        final Rect startBounds = mAnimationRect.scaledBitmapRect;
        final Rect finalBounds = AnimationUtility.getBitmapRectFromImageView(mPhotoView);

        if (finalBounds == null) {
            mPhotoView.animate().alpha(0);
            backgroundAnimator.start();
            return;
        }


        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            startScale = (float) startBounds.height() / finalBounds.height();
        } else {
            startScale = (float) startBounds.width() / finalBounds.width();
        }
        LogTool.d("finalBounds.width() =" + finalBounds.width() + ",finalBounds.height() =" +
                " " + finalBounds.height());

        int deltaTop = startBounds.top - finalBounds.top;
        int deltaLeft = startBounds.left - finalBounds.left;

        mPhotoView.setPivotY((mPhotoView.getHeight() - finalBounds.height()) / 2);
        mPhotoView.setPivotX((mPhotoView.getWidth() - finalBounds.width()) / 2);

        mPhotoView.animate().translationX(deltaLeft).translationY(deltaTop)
                .scaleY(startScale)
                .scaleX(startScale)
                .setDuration(ANIMATION_DURATION)
                .setInterpolator(new AccelerateDecelerateInterpolator());
        AnimatorSet animationSet = new AnimatorSet();
        animationSet.setDuration(ANIMATION_DURATION);
        animationSet
                .setInterpolator(new AccelerateDecelerateInterpolator());

        animationSet.playTogether(backgroundAnimator);

        animationSet.playTogether(ObjectAnimator.ofFloat(mPhotoView,
                "clipBottom", 0,
                AnimationRect.getClipBottom(mAnimationRect, finalBounds)));
        animationSet.playTogether(ObjectAnimator.ofFloat(mPhotoView,
                "clipRight", 0,
                AnimationRect.getClipRight(mAnimationRect, finalBounds)));
        animationSet.playTogether(ObjectAnimator.ofFloat(mPhotoView,
                "clipTop", 0, AnimationRect.getClipTop(mAnimationRect, finalBounds)));
        animationSet.playTogether(ObjectAnimator.ofFloat(mPhotoView,
                "clipLeft", 0, AnimationRect.getClipLeft(mAnimationRect, finalBounds)));

        animationSet.start();


    }

}

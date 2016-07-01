package com.jianfanjia.cn.ui.adapter;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;

import java.util.List;

import com.jianfanjia.cn.AppManager;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.bean.AnimationRect;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.tools.ImageShow;
import com.jianfanjia.cn.ui.interf.ViewPagerClickListener;
import me.iwf.photopicker.utils.AnimationUtility;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ShowPicPagerAdapter extends PagerAdapter {
    private static final int ANIMATION_DURATION = 300;
    private List<String> images;
    private LayoutInflater inflater;
    private ViewPagerClickListener viewPagerClickListener;
    private ImageShow imageShow;
    Context context;
    View.OnLongClickListener onLongClickListener;
    private AnimationRect rect;
    private boolean isFirst = true;

    public ShowPicPagerAdapter(Context context, List<String> imageList,
                               ViewPagerClickListener viewPagerClickListener, AnimationRect animationRect) {
        this.context = context;
        this.images = imageList;
        this.inflater = LayoutInflater.from(context);
        this.viewPagerClickListener = viewPagerClickListener;
        imageShow = ImageShow.getImageShow();
        this.rect = animationRect;
    }

    public void setOnLongClickListener(View.OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }

    public void deleteItem(int position) {
        if (position > -1 && position < images.size()) {
            images.remove(position);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        // TODO Auto-generated method stub
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        Log.i(this.getClass().getName(), images.get(position));
        View view = inflater.inflate(R.layout.viewpager_item_show_pic, null);
        final PhotoView imageView = (PhotoView) view.findViewById(R.id.image_item);
        if (!images.get(position).contains(Constant.DEFALUT_PIC_HEAD)) {
            imageShow.displayScreenWidthThumnailImage(context, images.get(position), imageView);
        } else {
            imageShow.displayLocalImage(images.get(position), imageView);
        }
        if (isFirst) {
            imageView.getViewTreeObserver()
                    .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                        @Override
                        public boolean onPreDraw() {

                            final Rect startBounds = new Rect(rect.scaledBitmapRect);
                            final Rect finalBounds = AnimationUtility
                                    .getBitmapRectFromImageView(imageView);

                            if (finalBounds == null) {
                                imageView.getViewTreeObserver().removeOnPreDrawListener(this);
                                return true;
                            }

                            float startScale = (float) finalBounds.width() / startBounds.width();

                            if (startScale * startBounds.height() > finalBounds.height()) {
                                startScale = (float) finalBounds.height() / startBounds.height();
                            }

                            int deltaTop = startBounds.top - finalBounds.top;
                            int deltaLeft = startBounds.left - finalBounds.left;

                            imageView.setPivotY(
                                    (imageView.getHeight() - finalBounds.height()) / 2);
                            imageView.setPivotX((imageView.getWidth() - finalBounds.width()) / 2);

                            imageView.setScaleX(1 / startScale);
                            imageView.setScaleY(1 / startScale);

                            imageView.setTranslationX(deltaLeft);
                            imageView.setTranslationY(deltaTop);

                            imageView.animate().translationY(0).translationX(0)
                                    .scaleY(1)
                                    .scaleX(1).setDuration(ANIMATION_DURATION)
                                    .setInterpolator(
                                            new AccelerateDecelerateInterpolator())
                                    .start();

                            imageView.getViewTreeObserver().removeOnPreDrawListener(this);
                            return true;
                        }
                    });
            isFirst = false;
        }
        imageView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                animationExit(imageView);
            }

            @Override
            public void onOutsidePhotoTap() {

            }
        });
        if (onLongClickListener != null) {
            imageView.setOnLongClickListener(onLongClickListener);
        }
        container.addView(view, 0);
        return view;
    }

    public void animationExit(PhotoView photoView) {

        if (Math.abs(photoView.getScale() - 1.0f) > 0.1f) {
            photoView.setScale(1, true);
            return;
        }

        animateClose(photoView);
    }

    private void animateClose(PhotoView photoView) {

        if (rect == null) {
            photoView.animate().alpha(0);
            return;
        }

        final Rect startBounds = rect.scaledBitmapRect;
        final Rect finalBounds = AnimationUtility.getBitmapRectFromImageView(photoView);

        if (finalBounds == null) {
            photoView.animate().alpha(0);
            return;
        }

        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            startScale = (float) startBounds.height() / finalBounds.height();
        } else {
            startScale = (float) startBounds.width() / finalBounds.width();
        }

        final float startScaleFinal = startScale;

        int deltaTop = startBounds.top - finalBounds.top;
        int deltaLeft = startBounds.left - finalBounds.left;

        photoView.setPivotY((photoView.getHeight() - finalBounds.height()) / 2);
        photoView.setPivotX((photoView.getWidth() - finalBounds.width()) / 2);

        photoView.animate().translationX(deltaLeft).translationY(deltaTop)
                .scaleY(startScaleFinal)
                .scaleX(startScaleFinal).setDuration(ANIMATION_DURATION)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        AppManager.getAppManager().finishActivity(((Activity) context));
                        ((Activity) context).overridePendingTransition(0, 0);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .start();


        /*AnimatorSet animationSet = new AnimatorSet();
        animationSet.setDuration(ANIMATION_DURATION);
        animationSet.setInterpolator(new AccelerateDecelerateInterpolator());

        animationSet.playTogether(ObjectAnimator.ofFloat(photoView,
                "clipBottom", 0,
                AnimationRect.getClipBottom(rect, finalBounds)));
        animationSet.playTogether(ObjectAnimator.ofFloat(photoView,
                "clipRight", 0,
                AnimationRect.getClipRight(rect, finalBounds)));
        animationSet.playTogether(ObjectAnimator.ofFloat(photoView,
                "clipTop", 0, AnimationRect.getClipTop(rect, finalBounds)));
        animationSet.playTogether(ObjectAnimator.ofFloat(photoView,
                "clipLeft", 0, AnimationRect.getClipLeft(rect, finalBounds)));
        animationSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                AppManager.getAppManager().finishActivity(((Activity) context));
                ((Activity) context).overridePendingTransition(0, 0);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });*//*AnimatorSet animationSet = new AnimatorSet();
        animationSet.setDuration(ANIMATION_DURATION);
        animationSet.setInterpolator(new AccelerateDecelerateInterpolator());

        animationSet.playTogether(ObjectAnimator.ofFloat(photoView,
                "clipBottom", 0,
                AnimationRect.getClipBottom(rect, finalBounds)));
        animationSet.playTogether(ObjectAnimator.ofFloat(photoView,
                "clipRight", 0,
                AnimationRect.getClipRight(rect, finalBounds)));
        animationSet.playTogether(ObjectAnimator.ofFloat(photoView,
                "clipTop", 0, AnimationRect.getClipTop(rect, finalBounds)));
        animationSet.playTogether(ObjectAnimator.ofFloat(photoView,
                "clipLeft", 0, AnimationRect.getClipLeft(rect, finalBounds)));
        animationSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                AppManager.getAppManager().finishActivity(((Activity) context));
                ((Activity) context).overridePendingTransition(0, 0);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });*/

//        animationSet.start();
    }

}

package com.jianfanjia.cn.ui.activity.common;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.BaseActivity;
import me.iwf.photopicker.entity.AnimationRect;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.tools.IntentUtil;
import com.jianfanjia.cn.ui.fragment.CommonShowPicFragment;
import com.jianfanjia.cn.view.viewpager.transfrom.ScaleInOutTransfromer;

/**
 * Description: com.jianfanjia.cn.ui.activity.common
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-07-01 16:11
 */
public class CommonShowPicActivity extends BaseActivity {

    private List<AnimationRect> mAnimationRect;
    private ArrayList<String> imageList = new ArrayList<>();

    @Bind(R.id.content_container)
    View backgroundView;

    @Bind(R.id.showpicPager)
    ViewPager viewPager;

    @Bind(R.id.pic_tip)
    TextView tipView;

    private int initPosition;
    private int totalCount = 0;

    private ColorDrawable backgroundColor;

    public static void intentTo(Context context, ArrayList<String> imageList, ArrayList<AnimationRect>
            animationRectList,int initPosition) {
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(Constant.IMAGE_LIST, imageList);
        bundle.putParcelableArrayList(Constant.ANIMATION_RECT_LIST, animationRectList);
        bundle.putInt(Constant.CURRENT_POSITION, initPosition);
        IntentUtil.startActivity(context, CommonShowPicActivity.class, bundle);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getDataFromIntent();
        this.initView(savedInstanceState);
    }

    private void initView(Bundle savedInstanceState) {
        viewPager.setAdapter(new ImagePagerAdapter(getSupportFragmentManager()));
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentSizeOfTotal(String.valueOf(position + 1) + "/" + totalCount);
            }

            @Override
            public void onPageScrollStateChanged(int scrollState) {
                if (scrollState != ViewPager.SCROLL_STATE_IDLE) {
                    final int childCount = viewPager.getChildCount();
                    for (int i = 0; i < childCount; i++) {
                        View child = viewPager.getChildAt(i);
                        if (child.getLayerType() != View.LAYER_TYPE_NONE) {
                            child.setLayerType(View.LAYER_TYPE_NONE, null);
                        }
                    }
                }
            }
        });
        viewPager.setCurrentItem(initPosition);
        setCurrentSizeOfTotal(String.valueOf(initPosition + 1) + "/" + totalCount);
        viewPager.setOffscreenPageLimit(1);
        viewPager.setPageTransformer(true, new ScaleInOutTransfromer());

        if (savedInstanceState != null) {
            showBackgroundImmediately();
        }
    }

    private void setCurrentSizeOfTotal(String text) {
        CommonShowPicActivity.this.tipView.setText(text);
    }

    private void getDataFromIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            initPosition = bundle.getInt(Constant.CURRENT_POSITION, 0);
            imageList = bundle.getStringArrayList(Constant.IMAGE_LIST);
            mAnimationRect = bundle.getParcelableArrayList(Constant.ANIMATION_RECT_LIST);
            totalCount = imageList.size();
        }
    }

    public void showBackgroundImmediately() {
        if (backgroundView.getBackground() == null) {
            backgroundColor = new ColorDrawable(Color.BLACK);
            backgroundView.setBackgroundColor(backgroundColor.getColor());
        }
    }

    public ObjectAnimator showBackgroundAnimate() {
        backgroundColor = new ColorDrawable(Color.BLACK);
        backgroundView.setBackgroundColor(backgroundColor.getColor());
        ObjectAnimator bgAnim = ObjectAnimator
                .ofInt(backgroundColor, "alpha", 0, 255);
        bgAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                backgroundView.setBackgroundColor(backgroundColor.getColor());
            }
        });
        return bgAnim;
    }

    private HashMap<Integer, CommonShowPicFragment> fragmentMap
            = new HashMap<>();

    private boolean alreadyAnimateIn = false;

    private class ImagePagerAdapter extends FragmentPagerAdapter {

        public ImagePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            CommonShowPicFragment fragment = fragmentMap.get(position);
            if (fragment == null) {

                boolean animateIn = (initPosition == position) && !alreadyAnimateIn;
                fragment = CommonShowPicFragment
                        .getInstance(imageList.get(position), mAnimationRect.get(position), animateIn);
                alreadyAnimateIn = true;
                fragmentMap.put(position, fragment);
            }

            return fragment;
        }

        //when activity is recycled, ViewPager will reuse fragment by theirs name, so
        //getItem wont be called, but we need fragmentMap to animate close operation
        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
            if (object instanceof Fragment) {
                fragmentMap.put(position, (CommonShowPicFragment) object);
            }
        }

        @Override
        public int getCount() {
            return imageList.size();
        }
    }


    @Override
    public void onBackPressed() {

        CommonShowPicFragment fragment = fragmentMap.get(viewPager.getCurrentItem());
        if (fragment != null) {
            backgroundColor = new ColorDrawable(Color.BLACK);
            ObjectAnimator bgAnim = ObjectAnimator.ofInt(backgroundColor, "alpha", 0);
            bgAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    backgroundView.setBackgroundColor(backgroundColor.getColor());
                }
            });
            bgAnim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    appManager.finishActivity(CommonShowPicActivity.this);
                    overridePendingTransition(-1, -1);
                }
            });
            fragment.animationExit(bgAnim);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_common_showpic;
    }
}

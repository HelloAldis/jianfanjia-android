package com.jianfanjia.cn.designer.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.jianfanjia.api.model.ProcessSection;
import com.jianfanjia.api.model.ProcessSectionItem;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.ui.adapter.SectionViewPageAdapter;
import com.jianfanjia.cn.designer.bean.ViewPagerItem;
import com.jianfanjia.cn.designer.config.Constant;
import com.jianfanjia.cn.designer.ui.interf.ViewPagerClickListener;
import com.jianfanjia.common.tool.DateFormatTool;
import com.jianfanjia.common.tool.LogTool;

/**
 * Description: com.jianfanjia.cn.designer.view
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-04-25 09:57
 */
public class ProcessDetailHeadView extends FrameLayout {

    private static final String TAG = ProcessDetailHeadView.class.getName();
    private static final int TOTAL_PROCESS = 7;// 7道工序

    private ViewPager mViewPager;

    private ProcessDetailHeadStateView mProcessDetailHeadStateView;

    private SectionViewPageAdapter sectionViewPageAdapter;

    private String[] proTitle = null;
    private List<ViewPagerItem> processList = new ArrayList<>();

    private boolean isCheckShow = false;

    private boolean isOpen = true;//验收view是否展开

    @Bind(R.id.indicator_image)
    ImageView indicatorImage;

    @Bind(R.id.rowBtnUp)
    ImageView rowBtnUp;

    @Bind(R.id.rowBtnUpLayout)
    RelativeLayout rowBtnUpLayout;

    @Bind(R.id.site_list_head_content_layout)
    LinearLayout site_list_head_content_layout;

    public ProcessDetailHeadView(Context context, AttributeSet attrs) {
        super(context, attrs);

        View view = inflate(context, R.layout.custom_processdetail_view, null);
        addView(view);
        ButterKnife.bind(this);

        rowBtnUpLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LogTool.d(TAG, "rowBtnUp animate");
                if (isOpen) {
                    closeAnim();
                } else {
                    openAnimate();
                }
            }
        });


        this.mViewPager = (ViewPager) findViewById(R.id.process_viewpager);
        this.mProcessDetailHeadStateView = (ProcessDetailHeadStateView) findViewById(R.id.process_headstateview);

        proTitle = getResources().getStringArray(R.array.site_procedure);

        initViewPager();
    }

    public void openCheckLayout() {
        if (isOpen) {

        } else {
            openAnimate();
        }
    }

    private void initViewPager() {
        for (int i = 0; i < proTitle.length; i++) {
            ViewPagerItem viewPagerItem = new ViewPagerItem();
            viewPagerItem.setResId(getContext().getResources()
                    .getIdentifier("icon_home_normal" + (i + 1), "drawable",
                            getContext().getPackageName()));
            viewPagerItem.setTitle(proTitle[i]);
            viewPagerItem.setDate("");
            processList.add(viewPagerItem);
        }
        for (int i = 0; i < 3; i++) {
            ViewPagerItem viewPagerItem = new ViewPagerItem();
            viewPagerItem.setResId(R.mipmap.icon_process_no);
            viewPagerItem.setTitle("");
            viewPagerItem.setDate("");
            processList.add(viewPagerItem);
        }
        sectionViewPageAdapter = new SectionViewPageAdapter(getContext(), processList,
                new ViewPagerClickListener() {

                    @Override
                    public void onClickItem(int potition) {
                        Log.i(TAG, "potition=" + potition);
                        if (potition < TOTAL_PROCESS) {
                            mViewPager.setCurrentItem(potition);
                            sectionViewPageAdapter.notifyDataSetChanged();
                        }
                    }

                });
        mViewPager.setAdapter(sectionViewPageAdapter);
    }

    public void setOnPageScrollListener(ViewPager.OnPageChangeListener onPageScrollListener) {
        mViewPager.addOnPageChangeListener(onPageScrollListener);
    }

    public void setCurrentItem(int pos){
        mViewPager.setCurrentItem(pos);
        sectionViewPageAdapter.notifyDataSetChanged();
    }

    public void setScrollHeadTime(List<ProcessSection> processSections) {
        if (processSections != null) {
            for (int i = 0; i < proTitle.length; i++) {
                ViewPagerItem viewPagerItem = sectionViewPageAdapter.getList()
                        .get(i);
                if (processSections.get(i).getStart_at() > 0) {
                    viewPagerItem.setDate(DateFormatTool.covertLongToString(
                            processSections.get(i).getStart_at(), "M.dd")
                            + "-"
                            + DateFormatTool.covertLongToString(processSections
                            .get(i).getEnd_at(), "M.dd"));
                }
                if (processSections.get(i).getStatus().equals(Constant.NO_START)) {
                    int drawableId = getResources()
                            .getIdentifier("icon_home_normal" + (i + 1),
                                    "mipmap",
                                    getContext().getPackageName());
                    viewPagerItem.setResId(drawableId);
                } else if (processSections.get(i).getStatus().equals(Constant.FINISHED)) {
                    int drawableId = getResources()
                            .getIdentifier("icon_home_checked" + (i + 1),
                                    "mipmap",
                                    getContext().getPackageName());
                    viewPagerItem.setResId(drawableId);
                } else {
                    int drawableId = getResources()
                            .getIdentifier("icon_home_normal_" + (i + 1),
                                    "mipmap",
                                    getContext().getPackageName());
                    viewPagerItem.setResId(drawableId);
                }
            }
            sectionViewPageAdapter.notifyDataSetChanged();
        }
    }

    public void changeProcessStateShow(ProcessSection processSection, boolean isResetCheckHead) {
        mProcessDetailHeadStateView.changeCheckLayoutState(processSection);
        changeCheckHead(processSection, isResetCheckHead);
    }

    public void changeCheckHead(ProcessSection processSection, boolean isResetCheckHead) {
        if (processSection.getName().equals("kai_gong")
                || processSection.getName().equals("chai_gai")) {
            indicatorImage.setVisibility(GONE);

            if (isCheckShow) {
                hideCheckHead();
                isCheckShow = false;
            }
            LogTool.d(TAG, "dismiss head");
        } else {
            indicatorImage.setVisibility(VISIBLE);

            if(!isResetCheckHead){
                isResetCheckHead = isResetCheckHead(processSection, isResetCheckHead);
            }

            if (isResetCheckHead) {
                openCheckLayout();
            }

            if (!isCheckShow) {
                showCheckHead();
                isCheckShow = true;
            }
        }
    }

    private boolean isResetCheckHead(ProcessSection processSection, boolean isResetCheckHead) {
        int finishCount = 0;
        for (ProcessSectionItem processSectionItem : processSection.getItems()) {
            if (processSectionItem.getStatus().equals(Constant.FINISHED)) {
                finishCount++;
            }
        }
        if (finishCount == processSection.getItems().size()) {
            isResetCheckHead = true;
        }
        return isResetCheckHead;
    }

    private void openAnimate() {
        rowBtnUp.animate().rotationBy(180).setDuration(200).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isOpen = true;
                openCheckHead();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).start();
    }

    private void closeAnim() {
        rowBtnUp.animate().rotationBy(-180).setDuration(200).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isOpen = false;
                closeCheckHead();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).start();

    }

    public void openCheckHead() {
        site_list_head_content_layout.measure(0, 0);
        final int aniHeight = site_list_head_content_layout.getHeight();
        LogTool.d(TAG, "open aniHeight =" + aniHeight);
        final int initHeight = this.getHeight();
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setIntValues(0, aniHeight);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(300);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ProcessDetailHeadView.this
                        .getLayoutParams();
                layoutParams.height = initHeight + (Integer) animation.getAnimatedValue();
                ProcessDetailHeadView.this.setLayoutParams(layoutParams);
            }
        });
        valueAnimator.start();
    }

    public void closeCheckHead() {
        site_list_head_content_layout.measure(0, 0);
        final int aniHeight = site_list_head_content_layout.getHeight();
        LogTool.d(TAG, "close aniHeight =" + aniHeight);
        final int initHeight = this.getHeight();
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setIntValues(0, aniHeight);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(300);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ProcessDetailHeadView.this
                        .getLayoutParams();
                layoutParams.height = initHeight - (Integer) animation.getAnimatedValue();
                ProcessDetailHeadView.this.setLayoutParams(layoutParams);
            }
        });
        valueAnimator.start();
    }

    public void showCheckHead() {
        mProcessDetailHeadStateView.measure(0, 0);
        int aniHeight;
        if (isOpen) {
            aniHeight = mProcessDetailHeadStateView.getHeight();
        } else {
            aniHeight = mProcessDetailHeadStateView.getHeight() - site_list_head_content_layout.getHeight();
        }
        LogTool.d(TAG, "hide aniHeight =" + aniHeight);
        final int initHeight = this.getHeight();
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setIntValues(0, aniHeight);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(300);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ProcessDetailHeadView.this
                        .getLayoutParams();
                layoutParams.height = initHeight + (Integer) animation.getAnimatedValue();
                ProcessDetailHeadView.this.setLayoutParams(layoutParams);
            }
        });
        valueAnimator.start();
    }

    public void hideCheckHead() {
        mProcessDetailHeadStateView.measure(0, 0);
        int aniHeight;
        if (isOpen) {
            aniHeight = mProcessDetailHeadStateView.getHeight();
        } else {
            aniHeight = mProcessDetailHeadStateView.getHeight() - site_list_head_content_layout.getHeight();
        }
        LogTool.d(TAG, "hide aniHeight =" + aniHeight);
        final int initHeight = this.getHeight();
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setIntValues(0, aniHeight);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(300);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ProcessDetailHeadView.this
                        .getLayoutParams();
                layoutParams.height = initHeight - (Integer) animation.getAnimatedValue();
                ProcessDetailHeadView.this.setLayoutParams(layoutParams);
            }
        });
        valueAnimator.start();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        // 新建一个Bundle
        Bundle bundle = new Bundle();
        // 保存view基本的状态，调用父类方法即可
        bundle.putParcelable("instanceState", super.onSaveInstanceState());
        // 保存我们自己的数据
        bundle.putBoolean("currentIsOpen", this.isOpen);
        bundle.putBoolean("currentIsCheckShow", this.isCheckShow);
        // 当然还可以继续保存其他数据
        // 返回bundle对象
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        // 判断该对象是否是我们保存的
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            // 把我们自己的数据恢复
            this.isOpen = bundle.getBoolean("currentIsOpen");
            this.isCheckShow = bundle.getBoolean("currentIsCheckShow");
            // 可以继续恢复之前的其他数据
            // 恢复view的基本状态
            state = bundle.getParcelable("instanceState");
        }
        // 如果不是我们保存的对象，则直接调用父类的方法进行恢复
        super.onRestoreInstanceState(state);
    }



}

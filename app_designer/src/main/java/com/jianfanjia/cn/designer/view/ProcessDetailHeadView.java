package com.jianfanjia.cn.designer.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import com.jianfanjia.api.model.ProcessSection;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.adapter.SectionViewPageAdapter;
import com.jianfanjia.cn.designer.bean.ViewPagerItem;
import com.jianfanjia.cn.designer.config.Constant;
import com.jianfanjia.cn.designer.interf.ViewPagerClickListener;
import com.jianfanjia.common.tool.DateFormatTool;

/**
 * Description: com.jianfanjia.cn.designer.view
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-04-25 09:57
 */
public class ProcessDetailHeadView extends LinearLayout {

    private static final String TAG = ProcessDetailHeadView.class.getName();
    private static final int TOTAL_PROCESS = 7;// 7道工序

    private ViewPager mViewPager;

    private ProcessDetailHeadStateView mProcessDetailHeadStateView;

    private SectionViewPageAdapter sectionViewPageAdapter;

    private String[] proTitle = null;
    private List<ViewPagerItem> processList = new ArrayList<>();

    public ProcessDetailHeadView(Context context, AttributeSet attrs) {
        super(context, attrs);

        View view = inflate(context, R.layout.custom_processdetail_view, null);
        addView(view);

        this.mViewPager = (ViewPager) findViewById(R.id.process_viewpager);
        this.mProcessDetailHeadStateView = (ProcessDetailHeadStateView) findViewById(R.id.process_headstateview);

        proTitle = getResources().getStringArray(R.array.site_procedure);

        initViewPager();
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

    public void changeProcessStateShow(ProcessSection processSection,boolean isFirst){
        mProcessDetailHeadStateView.changeCheckLayoutState(processSection,isFirst);
    }


}

package com.jianfanjia.cn.designer.ui.activity.my;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.ui.adapter.MyFragmentPagerAdapter;
import com.jianfanjia.cn.designer.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.designer.bean.SelectItem;
import com.jianfanjia.cn.designer.config.Constant;
import com.jianfanjia.cn.designer.ui.fragment.NoticeFragment;
import com.jianfanjia.cn.designer.view.MainHeadView;


/**
 * Description:通知
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class NoticeActivity extends BaseSwipeBackActivity {
    private static final String TAG = NoticeActivity.class.getName();

    public static final String TAB_TYPE = "tab_type";

    public static final int TAB_TYPE_PROCESS = 3;

    @Bind(R.id.my_notice_head_layout)
    MainHeadView mainHeadView;

    @Bind(R.id.tabLyout)
    TabLayout tabLayout;

    @Bind(R.id.viewpager)
    ViewPager mPager;

    private int initPos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent(){
        initPos = getIntent().getIntExtra(TAB_TYPE,0);
    }

    private void initView() {
        initMainHeadView();
        mPager.setOffscreenPageLimit(1);
        setupViewPager(mPager);
        tabLayout.setupWithViewPager(mPager);
        mPager.setCurrentItem(initPos);
    }

    private void initMainHeadView() {
        mainHeadView.setMianTitle(getResources().getString(R.string.my_notice));
        mainHeadView.setLayoutBackground(R.color.head_layout_bg);
        mainHeadView.setDividerVisable(View.GONE);
    }

    @OnClick(R.id.head_back_layout)
    public void onClick() {
        appManager.finishActivity(this);
    }

    private void setupViewPager(ViewPager viewPager) {
        List<SelectItem> listViews = new ArrayList<>();
        SelectItem allItem = new SelectItem(NoticeFragment.newInstance(Constant.ALL), getResources().getString(R
                .string.all_notice));
        SelectItem sysItem = new SelectItem(NoticeFragment.newInstance(Constant.SYSTEM), getResources().getString(R
                .string.system_notice));
        SelectItem reqItem = new SelectItem(NoticeFragment.newInstance(Constant.REQUIRE), getResources().getString(R
                .string.req_notice));
        SelectItem siteItem = new SelectItem(NoticeFragment.newInstance(Constant.SITE), getResources().getString(R
                .string.site_notice));
        listViews.add(allItem);
        listViews.add(sysItem);
        listViews.add(reqItem);
        listViews.add(siteItem);
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(fragmentManager, listViews);
        viewPager.setAdapter(adapter);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_notice;
    }

}

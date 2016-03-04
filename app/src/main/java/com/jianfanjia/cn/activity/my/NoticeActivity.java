package com.jianfanjia.cn.activity.my;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.SwipeBackActivity;
import com.jianfanjia.cn.adapter.MyFragmentPagerAdapter;
import com.jianfanjia.cn.bean.SelectItem;
import com.jianfanjia.cn.fragment.NoticeFragment;
import com.jianfanjia.cn.view.MainHeadView;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:通知
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class NoticeActivity extends SwipeBackActivity implements View.OnClickListener {
    private static final String TAG = AboutActivity.class.getName();
    public static final int TYPE_ALL = 0;
    public static final int TYPE_SYS = 1;
    public static final int TYPE_REQ = 2;
    public static final int TYPE_SITE = 3;
    private MainHeadView mainHeadView = null;
    private TabLayout tabLayout = null;
    private MyFragmentPagerAdapter adapter = null;
    private ViewPager mPager = null;
    private int initPosition = 0;

    @Override
    public void initView() {
        initMainHeadView();
        tabLayout = (TabLayout) findViewById(R.id.tabLyout);
        mPager = (ViewPager) findViewById(R.id.viewpager);
        mPager.setOffscreenPageLimit(3);
        setupViewPager(mPager);
        mPager.setCurrentItem(initPosition);
        tabLayout.setupWithViewPager(mPager);
    }

    private void initMainHeadView() {
        mainHeadView = (MainHeadView) findViewById(R.id.
                my_notice_head_layout);
        mainHeadView.setBackListener(this);
        mainHeadView.setMianTitle(getResources().getString(R.string.my_notice));
        mainHeadView.setLayoutBackground(R.color.head_layout_bg);
        mainHeadView.setDividerVisable(View.VISIBLE);
    }

    @Override
    public void setListener() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
            default:
                break;
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        List<SelectItem> listViews = new ArrayList<>();
        SelectItem allItem = new SelectItem(NoticeFragment.newInstance(TYPE_ALL), getResources().getString(R.string.all_notice));
        SelectItem sysItem = new SelectItem(NoticeFragment.newInstance(TYPE_SYS), getResources().getString(R.string.system_notice));
        SelectItem reqItem = new SelectItem(NoticeFragment.newInstance(TYPE_REQ), getResources().getString(R.string.req_notice));
        SelectItem siteItem = new SelectItem(NoticeFragment.newInstance(TYPE_SITE), getResources().getString(R.string.site_notice));
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

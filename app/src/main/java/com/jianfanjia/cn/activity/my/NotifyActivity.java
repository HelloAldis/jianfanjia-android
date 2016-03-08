package com.jianfanjia.cn.activity.my;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.SwipeBackActivity;
import com.jianfanjia.cn.adapter.MyFragmentPagerAdapter;
import com.jianfanjia.cn.bean.SelectItem;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.dao.impl.NotifyMessageDao;
import com.jianfanjia.cn.fragment.CaiGouNotifyFragment;
import com.jianfanjia.cn.fragment.DelayNotifyFragment;
import com.jianfanjia.cn.fragment.PayNotifyFragment;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.MainHeadView;

import java.util.ArrayList;
import java.util.List;

public class NotifyActivity extends SwipeBackActivity implements OnClickListener {
    private static final String TAG = NotifyActivity.class.getName();
    private MainHeadView mainHeadView = null;
    private TabLayout tabLayout = null;
    private MyFragmentPagerAdapter adapter = null;
    private ViewPager mPager = null;// 页卡内容
    private List<SelectItem> listViews = new ArrayList<SelectItem>(); // Tab页面列表
    private int initPosition = 0;
    protected NotifyMessageDao notifyMessageDao = null;

    @Override
    public void initView() {
        Intent intent = this.getIntent();
        String notifyType = intent.getStringExtra("Type");
        LogTool.d(TAG, "notifyType=" + notifyType);
        if (!TextUtils.isEmpty(notifyType)) {
            if (notifyType.equals(Constant.TYPE_CAIGOU_MSG)) {
                initPosition = 0;
            } else if (notifyType.equals(Constant.TYPE_PAY_MSG)) {
                initPosition = 1;
            } else if (notifyType.equals(Constant.TYPE_DELAY_MSG)) {
                initPosition = 2;
            }
        } else {
            initPosition = 0;
        }
        initMainHeadView();
        tabLayout = (TabLayout) findViewById(R.id.tabLyout);
        mPager = (ViewPager) findViewById(R.id.vPager);
        setupViewPager(mPager);
        mPager.setCurrentItem(initPosition);
        tabLayout.setupWithViewPager(mPager);
    }

    private void initMainHeadView() {
        mainHeadView = (MainHeadView) findViewById(R.id.notify_head_layout);
        mainHeadView.setBackListener(this);
        mainHeadView.setMianTitle(getResources().getString(R.string.my_tip));
        mainHeadView.setBackgroundTransparent();
        mainHeadView.setDividerVisable(View.GONE);
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
        List<SelectItem> listViews = new ArrayList<SelectItem>();
        SelectItem caigouItem = new SelectItem(new CaiGouNotifyFragment(), getResources().getString(R.string.caigouText));
        SelectItem payItem = new SelectItem(new PayNotifyFragment(), getResources().getString(R.string.fukuanText));
        SelectItem delayItem = new SelectItem(new DelayNotifyFragment(), getResources().getString(R.string.yanqiText));
        listViews.add(caigouItem);
        listViews.add(payItem);
        listViews.add(delayItem);
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(fragmentManager, listViews);
        viewPager.setAdapter(adapter);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_notify;
    }
}
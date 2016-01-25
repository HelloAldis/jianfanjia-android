package com.jianfanjia.cn.designer.activity.my;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.adapter.MyFragmentPagerAdapter;
import com.jianfanjia.cn.designer.base.BaseActivity;
import com.jianfanjia.cn.designer.bean.SelectItem;
import com.jianfanjia.cn.designer.config.Constant;
import com.jianfanjia.cn.designer.fragment.CaiGouNotifyFragment;
import com.jianfanjia.cn.designer.fragment.YanQiNotifyFragment;
import com.jianfanjia.cn.designer.tools.LogTool;
import com.jianfanjia.cn.designer.view.MainHeadView;

import java.util.ArrayList;
import java.util.List;

public class NotifyActivity extends BaseActivity implements OnClickListener {
    private static final String TAG = NotifyActivity.class.getName();
    private MainHeadView mainHeadView = null;
    private TabLayout tabLayout = null;
    private MyFragmentPagerAdapter adapter = null;
    private ViewPager mPager = null;// 页卡内容
    private List<SelectItem> listViews = new ArrayList<SelectItem>(); // Tab页面列表
    private int initialPosition = 0;

    @Override
    public void initView() {
        Intent intent = this.getIntent();
        String notifyType = intent.getStringExtra("Type");
        LogTool.d(TAG, "notifyType=" + notifyType);
        if (!TextUtils.isEmpty(notifyType)) {
            if (notifyType.equals(Constant.CAIGOU_NOTIFY)) {
                initialPosition = 0;
            } else if (notifyType.equals(Constant.YANQI_NOTIFY)) {
                initialPosition = 1;
            }
        } else {
            initialPosition = 0;
        }
        initMainHeadView();
        tabLayout = (TabLayout) findViewById(R.id.tabLyout);
        mPager = (ViewPager) findViewById(R.id.vPager);
        setupViewPager(mPager);
        mPager.setCurrentItem(initialPosition);
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
        SelectItem yanqiItem = new SelectItem(new YanQiNotifyFragment(), getResources().getString(R.string.yanqiText));
        listViews.add(caigouItem);
        listViews.add(yanqiItem);
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(fragmentManager, listViews);
        viewPager.setAdapter(adapter);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_notify;
    }
}
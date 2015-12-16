package com.jianfanjia.cn.designer.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
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
import com.jianfanjia.cn.designer.interf.SwitchFragmentListener;
import com.jianfanjia.cn.designer.tools.LogTool;
import com.jianfanjia.cn.designer.view.MainHeadView;
import com.jianfanjia.cn.designer.view.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fengliang
 * @ClassName: NotifyActivity
 * @Description: 提醒
 * @date 2015-9-11 上午9:49:10
 */
public class NotifyActivity extends BaseActivity implements OnClickListener {
    private static final String TAG = NotifyActivity.class.getName();
    private SwitchFragmentListener listener = null;
    private MainHeadView mainHeadView = null;
    private TabPageIndicator mPageIndicator = null;
    private MyFragmentPagerAdapter adapter = null;
    private ViewPager mPager = null;// 页卡内容
    private List<SelectItem> listViews = new ArrayList<SelectItem>(); // Tab页面列表
    private int initialPosition = 0;

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        try {
            listener = (SwitchFragmentListener) fragment;
        } catch (ClassCastException e) {
            LogTool.d(TAG, "e:" + e);
        }
    }

    @Override
    public void initView() {
        initMainHeadView();
        mPageIndicator = (TabPageIndicator) this
                .findViewById(R.id.page_indicator);
        mPager = (ViewPager) this.findViewById(R.id.vPager);
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
        initItem(initialPosition);
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
        // TODO Auto-generated method stub

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_layout:
                finish();
                break;
            default:
                break;
        }
    }

    private void initItem(int initialPosition) {
        SelectItem caigouItem = new SelectItem(new CaiGouNotifyFragment(),
                "采购提醒");
        SelectItem yanqiItem = new SelectItem(new YanQiNotifyFragment(), "改期提醒");
        listViews.add(caigouItem);
        listViews.add(yanqiItem);
        adapter = new MyFragmentPagerAdapter(fragmentManager, listViews);
        mPager.setAdapter(adapter);
        mPager.setOffscreenPageLimit(1);
        mPageIndicator.setViewPager(mPager, initialPosition);
        mPageIndicator
                .setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                    @Override
                    public void onPageSelected(int pos) {
                        LogTool.d(TAG, "pos=" + pos);
                    }

                    @Override
                    public void onPageScrolled(int arg0, float arg1, int arg2) {

                    }

                    @Override
                    public void onPageScrollStateChanged(int arg0) {

                    }
                });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_notify;
    }

}

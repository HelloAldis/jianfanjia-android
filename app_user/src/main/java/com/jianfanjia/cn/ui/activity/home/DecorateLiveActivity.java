package com.jianfanjia.cn.ui.activity.home;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.ui.adapter.DecorateLiveFragmentPagerAdapter;
import com.jianfanjia.cn.view.MainHeadView;

/**
 * Description: com.jianfanjia.cn.home
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-03-08 17:49
 */
public class DecorateLiveActivity extends BaseSwipeBackActivity {
    private static final String TAG = DecorateLiveActivity.class.getName();
    @Bind(R.id.decorate_live_head_layout)
    protected MainHeadView mainHeadView = null;
    @Bind(R.id.tablayout)
    protected TabLayout tabLayout = null;
    @Bind(R.id.viewpager)
    protected ViewPager viewPager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initView();
    }

    private void initView() {
        mainHeadView.setMianTitle(getString(R.string.decoration_live));
        mainHeadView.setDividerVisable(View.GONE);
        initViewPagerAndTab();
    }

    private void initViewPagerAndTab() {
        viewPager.setAdapter(new DecorateLiveFragmentPagerAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(1);
        tabLayout.setupWithViewPager(viewPager);
    }

    @OnClick({R.id.head_back_layout})
    protected void click(View view) {
        switch (view.getId()) {
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_decorate_live;
    }

}

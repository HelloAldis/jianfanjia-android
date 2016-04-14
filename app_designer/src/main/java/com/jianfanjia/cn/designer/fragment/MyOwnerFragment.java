package com.jianfanjia.cn.designer.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.adapter.MyOwnerFragmentPagerAdapter;
import com.jianfanjia.cn.designer.base.BaseFragment;
import com.jianfanjia.cn.designer.view.MainHeadView;

/**
 * Description:我的业主
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class MyOwnerFragment extends BaseFragment {

    @Bind(R.id.viewpager)
    protected ViewPager viewPager;

    @Bind(R.id.tablayout)
    protected TabLayout tabLayout;

    @Bind(R.id.my_owner_head_layout)
    protected MainHeadView mainHeadView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        initView();
        return view;
    }

    private void initView() {
        initHeadView();
        initViewPagerAndTab();
    }

    private void initHeadView() {
        mainHeadView.setBackLayoutVisable(View.GONE);
        mainHeadView.setMianTitle(getString(R.string.my_ower));
        mainHeadView.setBackgroundTransparent();
        mainHeadView.setDividerVisable(View.GONE);
    }

    private void initViewPagerAndTab() {
        viewPager.setAdapter(new MyOwnerFragmentPagerAdapter(getFragmentManager()));
        viewPager.setOffscreenPageLimit(1);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_my_owner;
    }
}

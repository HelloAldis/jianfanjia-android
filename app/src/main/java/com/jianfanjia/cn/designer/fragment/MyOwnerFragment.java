package com.jianfanjia.cn.designer.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.adapter.MyOwnerFragmentPagerAdapter;
import com.jianfanjia.cn.designer.base.BaseAnnotationFragment;
import com.jianfanjia.cn.designer.view.MainHeadView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Description:我的业主
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
@EFragment(R.layout.fragment_my_owner)
public class MyOwnerFragment extends BaseAnnotationFragment {

    @ViewById(R.id.viewpager)
    protected ViewPager viewPager;

    @ViewById(R.id.tablayout)
    protected TabLayout tabLayout;

    @ViewById(R.id.my_owner_head_layout)
    protected MainHeadView mainHeadView;

    @AfterViews
    protected void initAnnotationView(){
        initHeadView();
        initViewPagerAndTab();
    }

    protected void initHeadView(){
        mainHeadView.setBackLayoutVisable(View.GONE);
        mainHeadView.setMianTitle(getString(R.string.my_ower));

    }

    protected void initViewPagerAndTab(){
//        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(new MyOwnerFragmentPagerAdapter(getFragmentManager()));
        viewPager.setOffscreenPageLimit(1);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


}

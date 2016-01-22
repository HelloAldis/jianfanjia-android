package com.jianfanjia.cn.designer.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jianfanjia.cn.designer.fragment.RecycleViewFragment;

public class MyOwnerFragmentPagerAdapter extends FragmentPagerAdapter {

    private String[] tabs = new String[]{"待处理","处理中","已处理"};

    public MyOwnerFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Fragment getItem(int position) {
        return RecycleViewFragment.newInstance(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;  //没有找到child要求重新加载
    }

   /* @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }*/

}
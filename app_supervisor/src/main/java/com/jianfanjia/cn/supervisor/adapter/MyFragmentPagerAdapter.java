package com.jianfanjia.cn.supervisor.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import com.jianfanjia.cn.supervisor.bean.SelectItem;


public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<SelectItem> mList;

    public MyFragmentPagerAdapter(FragmentManager fm, List<SelectItem> mList) {
        super(fm);
        this.mList = mList;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mList.get(position).getDesc();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mList.get(position).getFragment();
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

}

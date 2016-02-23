package com.jianfanjia.cn.fragment;

import android.view.View;

import com.jianfanjia.cn.base.BaseFragment;

/**
 * Name: CommonFragment
 * User: fengliang
 * Date: 2016-02-23
 * Time: 16:44
 */
public class CommonFragment extends BaseFragment {
    private static final String TAG = CommonFragment.class.getName();
    protected boolean isVisible = false;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    protected void onVisible() {
        load();
    }

    protected void onInvisible() {

    }

    protected void load() {

    }

    @Override
    public void initView(View view) {

    }

    @Override
    public void setListener() {

    }

    @Override
    public int getLayoutId() {
        return -1;
    }
}

package com.jianfanjia.cn.fragment;

import android.view.View;

import com.jianfanjia.cn.base.BaseFragment;

/**
 * @author fengliang
 * @ClassName:CollectFragment
 * @Description: 收藏
 * @date 2015-8-26 下午1:07:52
 */
public class CollectFragment extends BaseFragment {
    private static final String TAG = CollectFragment.class.getName();
    protected boolean isVisible;

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

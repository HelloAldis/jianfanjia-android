package com.jianfanjia.cn.base;

import android.view.View;

/**
 * Description:Fragment基类
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 15:42
 */
public class BaseAnnotationFragment extends BaseFragment {
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
        return 0;
    }
}

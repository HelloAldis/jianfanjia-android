package com.jianfanjia.cn.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.BaseFragment;

/**
 * Description:装修美图
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class DecorationFragment extends BaseFragment {
    private static final String TAG = DecorationFragment.class.getName();
    private RecyclerView decoration_listview = null;

    @Override
    public void initView(View view) {

    }

    @Override
    public void setListener() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_decoration;
    }
}

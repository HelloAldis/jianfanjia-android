package com.jianfanjia.cn.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.view.MainHeadView;

/**
 * Description:装修美图
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class DecorationFragment extends BaseFragment {
    private static final String TAG = DecorationFragment.class.getName();
    private MainHeadView mainHeadView = null;
    private RecyclerView decoration_listview = null;

    @Override
    public void initView(View view) {
        initMainHeadView(view);
        decoration_listview = (RecyclerView) view.findViewById(R.id.decoration_listview);
    }

    private void initMainHeadView(View view) {
        mainHeadView = (MainHeadView) view.findViewById(R.id.dec_head);
        mainHeadView.setMianTitle(getResources().getString(R.string.decoration_img));
        mainHeadView.setLayoutBackground(R.color.head_layout_bg);
        mainHeadView.setRightTitleVisable(View.GONE);
        mainHeadView.setBackLayoutVisable(View.GONE);
    }

    @Override
    public void setListener() {

    }


    private void getDecorationImg() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_decoration;
    }
}

package com.jianfanjia.cn.fragment;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.BaseFragment;

/**
 * @author fengliang
 * @ClassName: DecorationImgFragment
 * @Description: 装修美图
 * @date 2015-8-26 下午1:07:52
 */
public class DecorationImgFragment extends BaseFragment {
    private static final String TAG = DecorationImgFragment.class.getName();
    private RecyclerView decoration_img_listview = null;

    @Override
    public void initView(View view) {
        decoration_img_listview = (RecyclerView) view.findViewById(R.id.decoration_img_listview);
        decoration_img_listview.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        decoration_img_listview.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void setListener() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_decoration_img;
    }

}

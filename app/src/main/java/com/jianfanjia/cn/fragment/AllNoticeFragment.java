package com.jianfanjia.cn.fragment;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.tools.UiHelper;
import com.jianfanjia.cn.view.library.PullToRefreshBase;
import com.jianfanjia.cn.view.library.PullToRefreshRecycleView;

/**
 * @author fengliang
 * @ClassName: AllNoticeFragment
 * @Description: 通知 全部
 * @date 2015-8-26 下午1:07:52
 */
public class AllNoticeFragment extends BaseFragment implements PullToRefreshBase.OnRefreshListener2<RecyclerView> {
    private static final String TAG = AllNoticeFragment.class.getName();
    private PullToRefreshRecycleView all_notice_listview = null;

    public static AllNoticeFragment newInstance() {
        AllNoticeFragment allFragment = new AllNoticeFragment();
        return allFragment;
    }

    @Override
    public void initView(View view) {
        all_notice_listview = (PullToRefreshRecycleView) view.findViewById(R.id.all_notice_listview);
        all_notice_listview.setMode(PullToRefreshBase.Mode.BOTH);
        all_notice_listview.setLayoutManager(new LinearLayoutManager(getActivity()));
        all_notice_listview.setHasFixedSize(true);
        all_notice_listview.setItemAnimator(new DefaultItemAnimator());
        all_notice_listview.addItemDecoration(UiHelper.buildDefaultHeightDecoration(getActivity().getApplicationContext()));
    }

    @Override
    public void setListener() {
        all_notice_listview.setOnRefreshListener(this);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
        all_notice_listview.onRefreshComplete();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
        all_notice_listview.onRefreshComplete();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_all_notice;
    }

}

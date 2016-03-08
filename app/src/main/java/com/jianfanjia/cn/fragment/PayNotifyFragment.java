package com.jianfanjia.cn.fragment;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.PayNotifyAdapter;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.bean.NotifyMessage;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.UiHelper;
import com.jianfanjia.cn.view.library.PullToRefreshBase;
import com.jianfanjia.cn.view.library.PullToRefreshRecycleView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fengliang
 * @ClassName: PayNotifyFragment
 * @Description: 付款提醒
 * @date 2015-8-26 下午1:08:44
 */
public class PayNotifyFragment extends BaseFragment implements PullToRefreshBase.OnRefreshListener2<RecyclerView> {
    private static final String TAG = PayNotifyFragment.class.getName();
    private PullToRefreshRecycleView payListView = null;
    private RelativeLayout emptyLayout = null;
    private RelativeLayout errorLayout = null;
    private List<NotifyMessage> payList = new ArrayList<>();
    private PayNotifyAdapter payAdapter = null;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUserVisibleHint(true);
        if (null != payList && payList.size() > 0) {
            payAdapter = new PayNotifyAdapter(getActivity(), payList);
            payListView.setAdapter(payAdapter);
            payListView.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);
            errorLayout.setVisibility(View.GONE);
        } else {
            payListView.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.VISIBLE);
            errorLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void initView(View view) {
        emptyLayout = (RelativeLayout) view.findViewById(R.id.empty_include);
        ((TextView) emptyLayout.findViewById(R.id.empty_text)).setText(getString(R.string.empty_view_no_pay_data));
        ((ImageView) emptyLayout.findViewById(R.id.empty_img)).setImageResource(R.mipmap.icon_pay);
        errorLayout = (RelativeLayout) view.findViewById(R.id.error_include);
        payListView = (PullToRefreshRecycleView) view.findViewById(R.id.tip_pay__listview);
        payListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        payListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        payListView.setItemAnimator(new DefaultItemAnimator());
        payListView.addItemDecoration(UiHelper.buildDefaultHeightDecoration(getActivity().getApplicationContext()));
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            LogTool.d(TAG, "PayNotifyFragment 可见");
            initData();
        } else {
            LogTool.d(TAG, "PayNotifyFragment 不可见");
        }
    }

    private void initData() {
        payList.clear();
        List<NotifyMessage> payMsgList = notifyMessageDao.getNotifyListByType(Constant.TYPE_PAY_MSG, dataManager.getUserId());
        LogTool.d(TAG, "payMsgList:" + payMsgList);
        payList.addAll(payMsgList);
    }

    @Override
    public void setListener() {
        payListView.setOnRefreshListener(this);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
        initData();
        payAdapter.notifyDataSetChanged();
        payListView.onRefreshComplete();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
        payListView.onRefreshComplete();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_notify_fukuan;
    }

}

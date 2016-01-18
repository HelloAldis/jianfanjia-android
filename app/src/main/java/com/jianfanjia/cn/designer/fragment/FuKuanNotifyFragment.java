package com.jianfanjia.cn.designer.fragment;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;

import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.adapter.PayNotifyAdapter;
import com.jianfanjia.cn.designer.base.BaseFragment;
import com.jianfanjia.cn.designer.bean.NotifyMessage;
import com.jianfanjia.cn.designer.config.Constant;
import com.jianfanjia.cn.designer.tools.LogTool;
import com.jianfanjia.cn.designer.view.baseview.HorizontalDividerItemDecoration;
import com.jianfanjia.cn.designer.view.library.PullToRefreshBase;
import com.jianfanjia.cn.designer.view.library.PullToRefreshRecycleView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fengliang
 * @ClassName: FuKuanNotifyFragment
 * @Description: 付款提醒
 * @date 2015-8-26 下午1:08:44
 */
public class FuKuanNotifyFragment extends BaseFragment implements PullToRefreshBase.OnRefreshListener2<RecyclerView> {
    private static final String TAG = FuKuanNotifyFragment.class.getName();
    private PullToRefreshRecycleView fukuanListView = null;
    private RelativeLayout emptyLayout = null;
    private RelativeLayout errorLayout = null;
    private List<NotifyMessage> payList = new ArrayList<NotifyMessage>();
    private PayNotifyAdapter payAdapter = null;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUserVisibleHint(true);
        if (null != payList && payList.size() > 0) {
            payAdapter = new PayNotifyAdapter(getActivity(), payList);
            fukuanListView.setAdapter(payAdapter);
            fukuanListView.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);
            errorLayout.setVisibility(View.GONE);
        } else {
            fukuanListView.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.VISIBLE);
            errorLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void initView(View view) {
        emptyLayout = (RelativeLayout) view.findViewById(R.id.empty_include);
        errorLayout = (RelativeLayout) view.findViewById(R.id.error_include);
        fukuanListView = (PullToRefreshRecycleView) view.findViewById(R.id.tip_pay__listview);
        fukuanListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        fukuanListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        fukuanListView.setItemAnimator(new DefaultItemAnimator());
        Paint paint = new Paint();
        paint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()));
        paint.setAlpha(0);
        paint.setAntiAlias(true);
        fukuanListView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).paint(paint).showLastDivider().build());
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            LogTool.d(TAG, "FuKuanNotifyFragment 可见");
            initData();
        } else {
            LogTool.d(TAG, "FuKuanNotifyFragment 不可见");
        }
    }

    private void initData() {
        payList.clear();
        List<NotifyMessage> payMsgList = notifyMessageDao.getNotifyListByType(Constant.FUKUAN_NOTIFY);
        LogTool.d(TAG, "payMsgList:" + payMsgList);
        payList.addAll(payMsgList);
    }

    @Override
    public void setListener() {
        fukuanListView.setOnRefreshListener(this);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
        initData();
        payAdapter.notifyDataSetChanged();
        fukuanListView.onRefreshComplete();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
        fukuanListView.onRefreshComplete();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_fukuan_notify;
    }

}

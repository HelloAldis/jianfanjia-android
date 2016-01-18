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
import com.jianfanjia.cn.designer.adapter.CaiGouNotifyAdapter;
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
 * @ClassName: CaiGouNotifyFragment
 * @Description: 采购提醒
 * @date 2015-8-26 下午1:07:52
 */
public class CaiGouNotifyFragment extends BaseFragment implements PullToRefreshBase.OnRefreshListener2<RecyclerView> {
    private static final String TAG = CaiGouNotifyFragment.class.getName();
    private PullToRefreshRecycleView caigouListView = null;
    private RelativeLayout emptyLayout = null;
    private RelativeLayout errorLayout = null;
    private List<NotifyMessage> caigouList = new ArrayList<NotifyMessage>();
    private CaiGouNotifyAdapter caiGouAdapter = null;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUserVisibleHint(true);
        if (null != caigouList && caigouList.size() > 0) {
            caiGouAdapter = new CaiGouNotifyAdapter(getActivity(), caigouList);
            caigouListView.setAdapter(caiGouAdapter);
            caigouListView.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);
            errorLayout.setVisibility(View.GONE);
        } else {
            caigouListView.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.VISIBLE);
            errorLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void initView(View view) {
        emptyLayout = (RelativeLayout) view.findViewById(R.id.empty_include);
        errorLayout = (RelativeLayout) view.findViewById(R.id.error_include);
        caigouListView = (PullToRefreshRecycleView) view
                .findViewById(R.id.tip_caigou__listview);
        caigouListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        caigouListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        caigouListView.setItemAnimator(new DefaultItemAnimator());
        Paint paint = new Paint();
        paint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()));
        paint.setAlpha(0);
        paint.setAntiAlias(true);
        caigouListView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).paint(paint).showLastDivider().build());
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            LogTool.d(TAG, "CaiGouNotifyFragment 可见");
            initData();
        } else {
            LogTool.d(TAG, "CaiGouNotifyFragment 不可见");
        }
    }

    private void initData() {
        caigouList.clear();
        List<NotifyMessage> caigouMsgList = notifyMessageDao
                .getNotifyListByType(Constant.CAIGOU_NOTIFY);
        LogTool.d(TAG, "caigouMsgList:" + caigouMsgList);
        caigouList.addAll(caigouMsgList);
    }

    @Override
    public void setListener() {
        caigouListView.setOnRefreshListener(this);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
        initData();
        caiGouAdapter.notifyDataSetChanged();
        caigouListView.onRefreshComplete();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
        caigouListView.onRefreshComplete();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_caigou_notify;
    }

}

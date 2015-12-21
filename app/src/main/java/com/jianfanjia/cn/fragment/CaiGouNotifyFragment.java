package com.jianfanjia.cn.fragment;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.CaiGouNotifyAdapter;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.bean.NotifyMessage;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.baseview.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fengliang
 * @ClassName: CaiGouNotifyFragment
 * @Description: 采购提醒
 * @date 2015-8-26 下午1:07:52
 */
public class CaiGouNotifyFragment extends BaseFragment {
    private static final String TAG = CaiGouNotifyFragment.class.getName();
    private RecyclerView caigouListView = null;
    private List<NotifyMessage> caigouList = new ArrayList<NotifyMessage>();
    private CaiGouNotifyAdapter caiGouAdapter = null;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        caiGouAdapter = new CaiGouNotifyAdapter(getActivity(), caigouList);
        caigouListView.setAdapter(caiGouAdapter);
    }

    @Override
    public void initView(View view) {
        caigouListView = (RecyclerView) view
                .findViewById(R.id.tip_caigou__listview);
        caigouListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        caigouListView.setItemAnimator(new DefaultItemAnimator());
        Paint paint = new Paint();
        paint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
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
        caigouList = notifyMessageDao
                .getNotifyListByType(Constant.CAIGOU_NOTIFY);
        LogTool.d(TAG, "caigouList:" + caigouList);
    }

    @Override
    public void setListener() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_caigou_notify;
    }

}

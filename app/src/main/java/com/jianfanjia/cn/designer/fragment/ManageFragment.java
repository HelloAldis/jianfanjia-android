package com.jianfanjia.cn.designer.fragment;

import android.graphics.Paint;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.adapter.MySiteAdapter;
import com.jianfanjia.cn.designer.base.BaseFragment;
import com.jianfanjia.cn.designer.view.MainHeadView;
import com.jianfanjia.cn.designer.view.baseview.HorizontalDividerItemDecoration;
import com.jianfanjia.cn.designer.view.library.PullToRefreshBase;
import com.jianfanjia.cn.designer.view.library.PullToRefreshRecycleView;

/**
 * Description:工地管理
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class ManageFragment extends BaseFragment implements PullToRefreshBase.OnRefreshListener2<RecyclerView> {
    private static final String TAG = ManageFragment.class.getName();
    private MainHeadView mainHeadView = null;
    private PullToRefreshRecycleView manage_pullfefresh = null;
    private MySiteAdapter mySiteAdapter = null;

    @Override
    public void initView(View view) {
        initMainHeadView(view);
        manage_pullfefresh = (PullToRefreshRecycleView) view.findViewById(R.id.manage_pullfefresh);
        manage_pullfefresh.setMode(PullToRefreshBase.Mode.BOTH);
        manage_pullfefresh.setLayoutManager(new LinearLayoutManager(getActivity()));
        manage_pullfefresh.setItemAnimator(new DefaultItemAnimator());
        Paint paint = new Paint();
        paint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()));
        paint.setAlpha(0);
        paint.setAntiAlias(true);
        manage_pullfefresh.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).paint(paint).showLastDivider().build());
    }

    private void initMainHeadView(View view) {
        mainHeadView = (MainHeadView) view.findViewById(R.id.manage_head);
        mainHeadView.setMianTitle("我的工地");
        mainHeadView.setBackgroundTransparent();
        mainHeadView.setRightTitleVisable(View.GONE);
        mainHeadView.setBackLayoutVisable(View.GONE);
    }

    @Override
    public void setListener() {
        manage_pullfefresh.setOnRefreshListener(this);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
        manage_pullfefresh.onRefreshComplete();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
        manage_pullfefresh.onRefreshComplete();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_manage2;
    }

}
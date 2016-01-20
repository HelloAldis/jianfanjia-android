package com.jianfanjia.cn.designer.fragment;

import android.graphics.Paint;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import com.jianfanjia.cn.designer.bean.Process;
import com.google.gson.reflect.TypeToken;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.adapter.MySiteAdapter;
import com.jianfanjia.cn.designer.base.BaseFragment;
import com.jianfanjia.cn.designer.http.JianFanJiaClient;
import com.jianfanjia.cn.designer.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.designer.tools.JsonParser;
import com.jianfanjia.cn.designer.tools.LogTool;
import com.jianfanjia.cn.designer.view.MainHeadView;
import com.jianfanjia.cn.designer.view.baseview.HorizontalDividerItemDecoration;
import com.jianfanjia.cn.designer.view.library.PullToRefreshBase;
import com.jianfanjia.cn.designer.view.library.PullToRefreshRecycleView;

import java.util.ArrayList;
import java.util.List;

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
    private List<Process> processList = new ArrayList<Process>();

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
        getProcessList();
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

    private void getProcessList() {
        JianFanJiaClient.get_Process_List(getActivity(), new ApiUiUpdateListener() {
            @Override
            public void preLoad() {

            }

            @Override
            public void loadSuccess(Object data) {
                LogTool.d(TAG, "data=" + data);
                processList = JsonParser.jsonToList(data.toString(), new TypeToken<List<Process>>() {
                }.getType());
                LogTool.d(TAG, "processList:" + processList);
                MySiteAdapter adapter = new MySiteAdapter(getActivity(), processList);
                manage_pullfefresh.setAdapter(adapter);
            }

            @Override
            public void loadFailture(String error_msg) {

            }
        }, this);
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

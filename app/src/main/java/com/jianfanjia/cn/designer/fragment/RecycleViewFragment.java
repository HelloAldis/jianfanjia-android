package com.jianfanjia.cn.designer.fragment;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.adapter.MyHandledRequirementAdapter;
import com.jianfanjia.cn.designer.application.MyApplication;
import com.jianfanjia.cn.designer.bean.RequirementInfo;
import com.jianfanjia.cn.designer.interf.ClickCallBack;
import com.jianfanjia.cn.designer.view.baseview.HorizontalDividerItemDecoration;
import com.jianfanjia.cn.designer.view.library.PullToRefreshBase;
import com.jianfanjia.cn.designer.view.library.PullToRefreshRecycleView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Description: com.jianfanjia.cn.designer.fragment
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-01-19 14:00
 */
@EFragment(R.layout.fragment_recycleview)
public class RecycleViewFragment extends Fragment {

    private int mNum;

    @ViewById(R.id.pull_refresh_recycle_view)
    protected PullToRefreshRecycleView pullrefresh;

    private MyHandledRequirementAdapter myHandledRequirementAdapter;

    private List<RequirementInfo> requirementInfos;

    /**
     * Create a new instance of CountingFragment, providing "num"
     * as an argument.
     *
     */
    public static RecycleViewFragment newInstance(int num) {
        RecycleViewFragment f = new RecycleViewFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);

        return f;
    }

    /**
     * When creating, retrieve this instance's number from its arguments.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNum = getArguments() != null ? getArguments().getInt("num") : 1;
    }

    @AfterViews
    protected void initRecycleView(){
        pullrefresh.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        pullrefresh.setLayoutManager(new LinearLayoutManager(getActivity()));
        pullrefresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<RecyclerView>() {
            @Override
            public void onRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                initData();
            }
        });
        myHandledRequirementAdapter = new MyHandledRequirementAdapter(getActivity(), new ClickCallBack() {
            @Override
            public void click(int position, int itemType) {

            }
        });
        pullrefresh.setAdapter(myHandledRequirementAdapter);
        Paint paint = new Paint();
        paint.setStrokeWidth(MyApplication.dip2px(getActivity(),8));
        paint.setColor(getResources().getColor(R.color.light_white_color));
        paint.setAntiAlias(true);
        pullrefresh.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).paint(paint).showLastDivider().build());
    }

    private void initData(){

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}

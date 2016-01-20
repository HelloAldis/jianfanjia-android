package com.jianfanjia.cn.designer.fragment;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.adapter.MyHandledRequirementAdapter;
import com.jianfanjia.cn.designer.application.MyApplication;
import com.jianfanjia.cn.designer.bean.RequirementInfo;
import com.jianfanjia.cn.designer.bean.RequirementList;
import com.jianfanjia.cn.designer.http.JianFanJiaClient;
import com.jianfanjia.cn.designer.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.designer.interf.ClickCallBack;
import com.jianfanjia.cn.designer.tools.JsonParser;
import com.jianfanjia.cn.designer.tools.LogTool;
import com.jianfanjia.cn.designer.view.baseview.HorizontalDividerItemDecoration;

import java.util.List;

/**
 * Description: com.jianfanjia.cn.designer.fragment
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-01-19 14:00
 */
public class RecycleViewFragment extends Fragment {

    private static final String TAG = "RecycleViewFragment";

    private final int FIRST_FRAGMENT = 0;
    private final int SECOND_FRAGMENT = 1;
    private final int THIRD_FRAGMENT = 2;

    private int mNum;

    protected RecyclerView pullrefresh;

    private MyHandledRequirementAdapter myHandledRequirementAdapter;

    private boolean isVisiable;

    private View view = null;

    /** 标志位，标志已经初始化完成 */
    private boolean isPrepared;
    /** 是否已被加载过一次，第二次就不再去请求数据了 */
    private boolean mHasLoadedOnce;

    private List<RequirementInfo> requirementInfos;

    /**
     * Create a new instance of CountingFragment, providing "num"
     * as an argument.
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

        LogTool.d(this.getClass().getName(), "num =" + mNum);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogTool.d(TAG, "onCreateView");
        if(view == null){
            view = inflater.inflate(R.layout.fragment_recycleview, container, false);
            initRecycleView();
            isPrepared = true;
            lazyLoad();
        }
        ViewGroup parent = (ViewGroup)view.getParent();
        if(parent != null) {
            parent.removeView(view);
        }
        return view;
    }

    /**
     * 可见
     */
    protected void onVisible() {
        lazyLoad();
    }


    /**
     * 不可见
     */
    protected void onInvisible() {

    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(getUserVisibleHint()) {
            isVisiable = true;
            onVisible();
        } else {
            isVisiable = false;
            onInvisible();
        }
    }



    private void lazyLoad(){
        if (!isPrepared || !isVisiable || mHasLoadedOnce) {
            return;
        }
        initData();
    }

    protected void initRecycleView() {
        pullrefresh = (RecyclerView) view.findViewById(R.id.pull_refresh_recycle_view);

//        pullrefresh.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        pullrefresh.setLayoutManager(new LinearLayoutManager(getActivity()));
       /* pullrefresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<RecyclerView>() {
            @Override
            public void onRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                initData();
            }
        });*/
        myHandledRequirementAdapter = new MyHandledRequirementAdapter(getActivity(), new ClickCallBack() {
            @Override
            public void click(int position, int itemType) {

            }
        });
        pullrefresh.setAdapter(myHandledRequirementAdapter);
        Paint paint = new Paint();
        paint.setStrokeWidth(MyApplication.dip2px(getActivity(), 8));
        paint.setColor(getResources().getColor(R.color.light_white_color));
        paint.setAntiAlias(true);
        pullrefresh.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).paint(paint).showLastDivider().build());
        LogTool.d(this.getClass().getName(), "initRecycle item count =" + myHandledRequirementAdapter.getItemCount());
    }

    private void initData() {
        JianFanJiaClient.getAllRequirementList(getActivity(), new ApiUiUpdateListener() {
            @Override
            public void preLoad() {

            }

            @Override
            public void loadSuccess(Object data) {
                LogTool.d(this.getClass().getName(), data.toString());
                mHasLoadedOnce = true;
                requirementInfos = JsonParser.jsonToList(data.toString(),new TypeToken<List<RequirementInfo>>(){}.getType());
                RequirementList requirementList = new RequirementList(requirementInfos);
                disposeData(requirementList);
            }

            @Override
            public void loadFailture(String error_msg) {

            }
        }, this);
    }


    private void disposeData(RequirementList requirementList) {
        switch (mNum){
            case FIRST_FRAGMENT:
                myHandledRequirementAdapter.addItem(requirementList.getUnHandleRequirementInfoList());
                break;
            case SECOND_FRAGMENT:
                myHandledRequirementAdapter.addItem(requirementList.getCommunicationRequirementInfoList());
                break;
            case THIRD_FRAGMENT:
                myHandledRequirementAdapter.addItem(requirementList.getOverRequirementInfoLists());
                break;

        }

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogTool.d(TAG, "onActivityCreated");
    }

}

package com.jianfanjia.cn.ui.activity.diary;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import butterknife.Bind;
import com.aldis.hud.Hud;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.HttpCode;
import com.jianfanjia.api.model.DiarySetInfo;
import com.jianfanjia.api.model.DiarySetInfoList;
import com.jianfanjia.api.request.common.GetMyDiarySetRequest;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.api.Api;
import com.jianfanjia.cn.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshBase;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshRecycleView;
import com.jianfanjia.cn.tools.UiHelper;
import com.jianfanjia.cn.ui.adapter.DiarySetListAdapter;
import com.jianfanjia.cn.view.MainHeadView;

/**
 * Description: com.jianfanjia.cn.ui.activity.diary
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-06-13 15:45
 */
public class DiarySetListActivity extends BaseSwipeBackActivity {

    @Bind(R.id.mainhv_diary)
    MainHeadView mMainHeadView;

    @Bind(R.id.pullrefresh_recycleview)
    PullToRefreshRecycleView mRecyclerView;

    private DiarySetListAdapter mDiarySetListAdapter;
    private List<DiarySetInfo> mDiaryInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        initMainView();
        initRecycleView();

    }

    private void initMainView() {
        mMainHeadView.setMianTitle(getString(R.string.my_diaryset));
        mMainHeadView.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appManager.finishActivity(DiarySetListActivity.class);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllDiarySet();
    }

    private void getAllDiarySet(){
        GetMyDiarySetRequest getMyDiarySetRequest = new GetMyDiarySetRequest();

        Api.getMyDiarySetList(getMyDiarySetRequest, new ApiCallback<ApiResponse<DiarySetInfoList>>() {
            @Override
            public void onPreLoad() {
                Hud.show(getUiContext());
            }

            @Override
            public void onHttpDone() {
                Hud.dismiss();
                mRecyclerView.onRefreshComplete();
            }

            @Override
            public void onSuccess(ApiResponse<DiarySetInfoList> apiResponse) {
                DiarySetInfoList diarySetInfoList = apiResponse.getData();
                if(diarySetInfoList != null){
                    mDiaryInfoList = diarySetInfoList.getDiarySets();
                    mDiarySetListAdapter.setList(mDiaryInfoList);
                }
            }

            @Override
            public void onFailed(ApiResponse<DiarySetInfoList> apiResponse) {
                makeTextShort(apiResponse.getErr_msg());
            }

            @Override
            public void onNetworkError(int code) {
                makeTextShort(HttpCode.NO_NETWORK_ERROR_MSG);
            }
        },this);
    }

    private void initRecycleView() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<RecyclerView>() {
            @Override
            public void onRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                getAllDiarySet();
            }
        });
        mRecyclerView.addItemDecoration(UiHelper.buildDefaultHeightDecoration(this));
        mDiarySetListAdapter = new DiarySetListAdapter(this, mDiaryInfoList);
        mDiarySetListAdapter.setHasAddDiarySet(true);
        mRecyclerView.setAdapter(mDiarySetListAdapter);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_diaryset_list;
    }
}

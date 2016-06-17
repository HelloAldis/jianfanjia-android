package com.jianfanjia.cn.ui.activity.diary;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.HttpCode;
import com.jianfanjia.api.model.DiaryInfo;
import com.jianfanjia.api.model.DiarySetInfo;
import com.jianfanjia.api.request.guest.GetDiarySetInfoRequest;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.api.Api;
import com.jianfanjia.cn.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.constant.IntentConstant;
import com.jianfanjia.cn.ui.adapter.DiarySetInfoAdapter;
import com.jianfanjia.cn.view.recycleview.itemdecoration.HorizontalDividerDecoration;
import com.jianfanjia.common.tool.TDevice;

/**
 * Description: com.jianfanjia.cn.ui.activity.diary
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-06-14 11:01
 */
public class DiarySetInfoActivity extends BaseSwipeBackActivity {

    @Bind(R.id.diary_recycleview)
    RecyclerView mRecyclerView;

    private DiarySetInfoAdapter mDiaryAdapter;

    private List<DiaryInfo> mDiaryInfoList;

    private ArrayList<DiarySetInfo> mDiarySetInfoList;

    private DiarySetInfo mDiarySetInfo;

    private String diarySetId;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mDiarySetInfo = (DiarySetInfo) bundle.getSerializable(IntentConstant.DIARYSET_INFO);
            mDiarySetInfoList = (ArrayList<DiarySetInfo>) bundle.getSerializable(IntentConstant.DIARYSET_INFO_LIST);
            if (mDiarySetInfo != null) {
                diarySetId = mDiarySetInfo.get_id();
            }
        }
    }

    public ArrayList<DiarySetInfo> getDiarySetInfoList() {
        return mDiarySetInfoList;
    }

    private void initView() {
        toolbar.setNavigationIcon(R.mipmap.icon_back);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initRecyclerView();

        getDiarySetDetail();
    }

    private void getDiarySetDetail() {
        if (diarySetId == null) return;
        GetDiarySetInfoRequest getDiarySetInfoRequest = new GetDiarySetInfoRequest();
        getDiarySetInfoRequest.setDiarySetid(diarySetId);

        Api.getDiarySetInfo(getDiarySetInfoRequest, new ApiCallback<ApiResponse<DiarySetInfo>>() {
            @Override
            public void onPreLoad() {
                showWaitDialog();
            }

            @Override
            public void onHttpDone() {
                hideWaitDialog();
            }

            @Override
            public void onSuccess(ApiResponse<DiarySetInfo> apiResponse) {
                DiarySetInfo diarySetInfo = apiResponse.getData();
                mDiarySetInfo.setDiaries(diarySetInfo.getDiaries());
                mDiarySetInfo.setView_count(diarySetInfo.getView_count());
                mDiaryAdapter.setDiarySetInfo(mDiarySetInfo);
            }

            @Override
            public void onFailed(ApiResponse<DiarySetInfo> apiResponse) {
                makeTextShort(apiResponse.getErr_msg());
            }

            @Override
            public void onNetworkError(int code) {
                makeTextShort(HttpCode.NO_NETWORK_ERROR_MSG);
            }
        });
    }

    private void initRecyclerView() {
        mDiaryAdapter = new DiarySetInfoAdapter(this, mDiaryInfoList);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new HorizontalDividerDecoration(TDevice.dip2px(this, 10),
                0, TDevice.dip2px(this, 10)));
        mRecyclerView.setAdapter(mDiaryAdapter);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_diaryinfo;
    }
}

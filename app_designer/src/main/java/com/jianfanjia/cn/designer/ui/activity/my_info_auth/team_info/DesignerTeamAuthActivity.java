package com.jianfanjia.cn.designer.ui.activity.my_info_auth.team_info;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.model.Team;
import com.jianfanjia.api.request.designer.DeleteOneTeamRequest;
import com.jianfanjia.api.request.designer.GetAllTeamRequest;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.api.Api;
import com.jianfanjia.cn.designer.base.BaseRecyclerViewAdapter;
import com.jianfanjia.cn.designer.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.designer.config.Global;
import com.jianfanjia.cn.designer.tools.IntentUtil;
import com.jianfanjia.cn.designer.ui.adapter.DesignerTeamAuthAdapter;
import com.jianfanjia.cn.designer.view.MainHeadView;
import com.jianfanjia.cn.designer.view.baseview.ItemSpaceDecoration;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshRecycleView;
import com.jianfanjia.common.tool.TDevice;

/**
 * Description: com.jianfanjia.cn.designer.ui.activity.my_info_auth.team_info
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-05-18 11:26
 */
public class DesignerTeamAuthActivity extends BaseSwipeBackActivity {

    @Bind(R.id.pullrefresh_recycleview)
    PullToRefreshRecycleView gridView;

    @Bind(R.id.designer_team_head_layout)
    MainHeadView mMainHeadView;

    DesignerTeamAuthAdapter mDesignerTeamAuthAdapter;

    private List<Team> mTeamList;

    private boolean isEditStatus = false;//判断当前页面是否是编辑页面

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        initMainView();
        initGridView();
        getAllTeam();
    }

    @OnClick({R.id.head_back_layout})
    protected void click(View view) {
        switch (view.getId()) {
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
        }
    }

    private void initGridView() {
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == 0 ? gridLayoutManager.getSpanCount() : 1;
            }
        });
        gridView.setLayoutManager(gridLayoutManager);
        gridView.setHasFixedSize(true);
        gridView.setItemAnimator(new DefaultItemAnimator());
        ItemSpaceDecoration decoration = new ItemSpaceDecoration(TDevice.dip2px(getApplicationContext(), 5));
        gridView.addItemDecoration(decoration);
        mDesignerTeamAuthAdapter = new DesignerTeamAuthAdapter(this, mTeamList, new BaseRecyclerViewAdapter
                .OnItemEditListener() {
            @Override
            public void onItemClick(int position) {
                intentToTeamDetail(mTeamList.get(position));
            }

            @Override
            public void onItemAdd() {

            }

            @Override
            public void onItemDelete(int position) {
                Team team = mTeamList.get(position);
                deleteOneTeam(team.get_id(), position);
            }
        });
        gridView.setAdapter(mDesignerTeamAuthAdapter);
    }

    private void intentToTeamDetail(Team team) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Global.TEAM_INFO, team);
        IntentUtil.startActivity(this, DesignerEditTeamActivity.class, bundle);
    }

    private void getAllTeam() {
        GetAllTeamRequest getAllTeamRequest = new GetAllTeamRequest();
        getAllTeamRequest.setFrom(0);
        getAllTeamRequest.setLimit(1000);

        Api.getAllTeam(getAllTeamRequest, new ApiCallback<ApiResponse<List<Team>>>() {
            @Override
            public void onPreLoad() {
                showWaitDialog();
            }

            @Override
            public void onHttpDone() {
                hideWaitDialog();
                gridView.onRefreshComplete();
            }

            @Override
            public void onSuccess(ApiResponse<List<Team>> apiResponse) {
                mTeamList = apiResponse.getData();

                mDesignerTeamAuthAdapter.setList(mTeamList);

            }

            @Override
            public void onFailed(ApiResponse<List<Team>> apiResponse) {

            }

            @Override
            public void onNetworkError(int code) {

            }
        });

    }

    private void deleteOneTeam(String teamId, final int position) {
        DeleteOneTeamRequest deleteOneTeamRequest = new DeleteOneTeamRequest();
        deleteOneTeamRequest.set_id(teamId);

        Api.deleteOneTeam(deleteOneTeamRequest, new ApiCallback<ApiResponse<String>>() {
            @Override
            public void onPreLoad() {
                showWaitDialog();
            }

            @Override
            public void onHttpDone() {
                hideWaitDialog();
            }

            @Override
            public void onSuccess(ApiResponse<String> apiResponse) {
                mDesignerTeamAuthAdapter.remove(position);
            }

            @Override
            public void onFailed(ApiResponse<String> apiResponse) {

            }

            @Override
            public void onNetworkError(int code) {

            }
        });
    }


    private void initMainView() {
        mMainHeadView.setMianTitle(getString(R.string.my_product));
        mMainHeadView.setRightTitle(getString(R.string.edit));
        mMainHeadView.setRightTitleColor(R.color.grey_color);
        mMainHeadView.setRightTextListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isEditStatus = !isEditStatus;
                mDesignerTeamAuthAdapter.setIsEdit(isEditStatus);
                mDesignerTeamAuthAdapter.notifyDataSetChanged();
                if (isEditStatus) {
                    mMainHeadView.setRightTitle(getString(R.string.finish));
                } else {
                    mMainHeadView.setRightTitle(getString(R.string.edit));
                }
            }
        });
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_designer_team_auth;
    }
}

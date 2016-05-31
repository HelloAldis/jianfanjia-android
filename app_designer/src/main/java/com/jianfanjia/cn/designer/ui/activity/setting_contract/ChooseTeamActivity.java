package com.jianfanjia.cn.designer.ui.activity.setting_contract;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.HttpCode;
import com.jianfanjia.api.model.Team;
import com.jianfanjia.api.request.designer.ConfigContractRequest;
import com.jianfanjia.api.request.designer.GetAllTeamRequest;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.api.Api;
import com.jianfanjia.cn.designer.base.BaseRecyclerViewAdapter;
import com.jianfanjia.cn.designer.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.designer.bean.ConfigContractInfo;
import com.jianfanjia.cn.designer.config.Global;
import com.jianfanjia.cn.designer.tools.IntentUtil;
import com.jianfanjia.cn.designer.ui.Event.UpdateEvent;
import com.jianfanjia.cn.designer.ui.activity.MainActivity;
import com.jianfanjia.cn.designer.ui.activity.my_info_auth.team_info.DesignerEditTeamActivity;
import com.jianfanjia.cn.designer.ui.adapter.ChooseTeamAdapter;
import com.jianfanjia.cn.designer.view.MainHeadView;
import com.jianfanjia.cn.designer.view.baseview.ItemSpaceDecoration;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshBase;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshRecycleView;
import com.jianfanjia.common.tool.TDevice;
import de.greenrobot.event.EventBus;

/**
 * Description: com.jianfanjia.cn.designer.ui.activity.setting_contract
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-05-31 16:23
 */
public class ChooseTeamActivity extends BaseSwipeBackActivity {

    public static final String CONFIG_CONTRACT_INFO = "config_contract_info";

    @Bind(R.id.pullrefresh_recycleview)
    PullToRefreshRecycleView gridView;

    @Bind(R.id.designer_team_head_layout)
    MainHeadView mMainHeadView;

    ChooseTeamAdapter mChooseTeamAdapter;

    private List<Team> mTeamList;

    private ConfigContractInfo mConfigContractInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        mConfigContractInfo = (ConfigContractInfo) intent.getSerializableExtra(CONFIG_CONTRACT_INFO);
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
        mChooseTeamAdapter = new ChooseTeamAdapter(this, mTeamList, new BaseRecyclerViewAdapter
                .OnItemEditListener() {
            @Override
            public void onItemClick(int position) {
                intentToTeamDetail(mTeamList.get(position));
            }

            @Override
            public void onItemAdd() {
                Bundle bundle = new Bundle();
                bundle.putInt(DesignerEditTeamActivity.INTENT_FROM_FLAG, DesignerEditTeamActivity.FROM_ADD_INTENT);
                startActivity(DesignerEditTeamActivity.class, bundle);
            }

            @Override
            public void onItemDelete(int position) {

            }
        });
        mChooseTeamAdapter.setOnItemChooseListener(new ChooseTeamAdapter.OnItemChooseListener() {
            @Override
            public void chooseItem(int position) {
                if (mChooseTeamAdapter.getCurrentChoosePos() != position) {
                    mChooseTeamAdapter.setCurrentChoosePos(position);
                }
                setMianHeadRightTitleEnable();
            }
        });
        gridView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<RecyclerView>() {
            @Override
            public void onRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                getAllTeam();
            }
        });
        gridView.setAdapter(mChooseTeamAdapter);
    }

    private void setMianHeadRightTitleEnable() {
        if (mChooseTeamAdapter.getCurrentChoosePos() != -1) {
            mMainHeadView.setRigthTitleEnable(true);
        } else {
            mMainHeadView.setRigthTitleEnable(false);
        }
    }


    private void intentToTeamDetail(Team team) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Global.TEAM_INFO, team);
        bundle.putInt(DesignerEditTeamActivity.INTENT_FROM_FLAG, DesignerEditTeamActivity.FROM_ADD_INTENT);
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

                mChooseTeamAdapter.setList(mTeamList);
            }

            @Override
            public void onFailed(ApiResponse<List<Team>> apiResponse) {
                makeTextShort(apiResponse.getErr_msg());
            }

            @Override
            public void onNetworkError(int code) {
                makeTextShort(HttpCode.NO_NETWORK_ERROR_MSG);
            }
        });

    }

    private void configStartTime(ConfigContractInfo configContractInfo) {
        ConfigContractRequest configContractRequest = new ConfigContractRequest();
        configContractRequest.setRequirementid(configContractInfo.getRequirementid());
        configContractRequest.setStart_at(configContractInfo.getStartAt());
        configContractInfo.setManager(configContractInfo.getManager());

        Api.configContract(configContractRequest, new ApiCallback<ApiResponse<String>>() {
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
                startActivity(MainActivity.class);
                overridePendingTransition(0, R.anim.slide_out_to_bottom);
                EventBus.getDefault().post(new UpdateEvent(null));
            }

            @Override
            public void onFailed(ApiResponse<String> apiResponse) {
                makeTextShort(apiResponse.getErr_msg());
            }

            @Override
            public void onNetworkError(int code) {
                makeTextShort(HttpCode.NO_NETWORK_ERROR_MSG);
            }
        });
    }

    private void initMainView() {
        mMainHeadView.setMianTitle(getString(R.string.config_contract));
        mMainHeadView.setRightTitle(getString(R.string.finish));
        mMainHeadView.setRigthTitleEnable(false);
        mMainHeadView.setRightTextListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConfigContractInfo.setManager(mTeamList.get(mChooseTeamAdapter.getCurrentChoosePos()).getManager());
                configStartTime(mConfigContractInfo);
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_designer_team_auth;
    }


}

package com.jianfanjia.cn.activity.requirement;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.HttpCode;
import com.jianfanjia.api.model.Requirement;
import com.jianfanjia.api.request.user.UpdateRequirementRequest;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.api.Api;
import com.jianfanjia.cn.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.constant.IntentConstant;
import com.jianfanjia.cn.fragment.EditBussinessRequirementFragment;
import com.jianfanjia.cn.fragment.EditHomeRequirementFragment;
import com.jianfanjia.cn.fragment.XuQiuFragment;
import com.jianfanjia.cn.interf.NotifyActivityStatusChange;
import com.jianfanjia.cn.view.MainHeadView;

/**
 * Description: com.jianfanjia.cn.activity
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-12-17 15:46
 */
public class UpdateRequirementActivity extends BaseSwipeBackActivity implements NotifyActivityStatusChange {

    @Bind(R.id.act_edit_req_head)
    protected MainHeadView mainHeadView;

    private EditHomeRequirementFragment editHomeRequirementFragment_;
    private EditBussinessRequirementFragment editBussinessRequirementFragment_;

    protected String status;//当前页面的状态，家装还是商装
    private Requirement requirementInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    public void initView() {
        mainHeadView.setMianTitle(getString(R.string.update_requirement));
        mainHeadView.setRightTitle(getString(R.string.confirm));
        mainHeadView.setRigthTitleEnable(false);

        Intent intent = getIntent();
        requirementInfo = (Requirement) intent.getSerializableExtra(IntentConstant.REQUIREMENT_INFO);
        if (requirementInfo != null) {
            status = requirementInfo.getDec_type();
            if (status != null) {
                initFragment();
            }
        }
    }

    protected void initFragment() {
        FragmentTransaction transaction = this.getSupportFragmentManager()
                .beginTransaction();
        switch (status) {
            case Global.DEC_TYPE_BUSINESS:
                editBussinessRequirementFragment_ = new EditBussinessRequirementFragment();
                editBussinessRequirementFragment_.setArguments(getBundle());
                transaction.replace(R.id.content_layout, editBussinessRequirementFragment_);
                break;
            case Global.DEC_TYPE_HOME:
                editHomeRequirementFragment_ = new EditHomeRequirementFragment();
                editHomeRequirementFragment_.setArguments(getBundle());
                transaction.replace(R.id.content_layout, editHomeRequirementFragment_);
                break;
        }
        transaction.commit();
    }

    private void resetRightTitleStatus() {
        switch (status) {
            case Global.DEC_TYPE_HOME:
                mainHeadView.setRigthTitleEnable(editHomeRequirementFragment_.isFinish());
                break;
            case Global.DEC_TYPE_BUSINESS:
                mainHeadView.setRigthTitleEnable(editBussinessRequirementFragment_.isFinish());
                break;
        }
    }

    @Override
    public void notifyStatusChange() {
        //重置完成按钮的状态
        resetRightTitleStatus();
    }

    protected void confirm() {
        Requirement requirementInfo = null;
        switch (status) {
            case Global.DEC_TYPE_HOME:
                requirementInfo = editHomeRequirementFragment_.getRequirementInfo();
                break;
            case Global.DEC_TYPE_BUSINESS:
                requirementInfo = editBussinessRequirementFragment_.getRequirementInfo();
                break;
        }
        UpdateRequirementRequest updateRequirementRequest = new UpdateRequirementRequest();
        updateRequirementRequest.setRequirement(requirementInfo);
        Api.updateRequirement(updateRequirementRequest, new ApiCallback<ApiResponse<Object>>() {
            @Override
            public void onPreLoad() {
                showWaitDialog();
            }

            @Override
            public void onHttpDone() {
                hideWaitDialog();
            }

            @Override
            public void onSuccess(ApiResponse<Object> apiResponse) {
                appManager.finishActivity(UpdateRequirementActivity.this);
            }

            @Override
            public void onFailed(ApiResponse<Object> apiResponse) {
                makeTextShort(apiResponse.getErr_msg());
            }

            @Override
            public void onNetworkError(int code) {
                makeTextShort(HttpCode.NO_NETWORK_ERROR_MSG);
            }
        });

    }

    @OnClick({R.id.head_back_layout, R.id.head_right_title})
    protected void click(View view) {
        switch (view.getId()) {
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
            case R.id.head_right_title:
                confirm();
                break;
        }
    }

    protected Bundle getBundle() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(IntentConstant.REQUIREMENT_INFO, requirementInfo);
        bundle.putInt(IntentConstant.REQUIREMENG_ACTION_TYPE, XuQiuFragment.REQUESTCODE_EDIT_REQUIREMENT);
        return bundle;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_edit_busi_req;
    }
}

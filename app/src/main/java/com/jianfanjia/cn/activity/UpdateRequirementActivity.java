package com.jianfanjia.cn.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.jianfanjia.cn.base.BaseAnnotationActivity;
import com.jianfanjia.cn.bean.RequirementInfo;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.fragment.EditBussinessRequirementFragment_;
import com.jianfanjia.cn.fragment.EditHomeRequirementFragment_;
import com.jianfanjia.cn.fragment.XuQiuFragment;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.NotifyActivityStatusChange;
import com.jianfanjia.cn.view.MainHeadView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Description: com.jianfanjia.cn.activity
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-12-17 15:46
 */
@EActivity(R.layout.activity_edit_busi_req)
public class UpdateRequirementActivity extends BaseAnnotationActivity implements NotifyActivityStatusChange{

    @ViewById(R.id.act_edit_req_head)
    protected MainHeadView mainHeadView;

    private EditHomeRequirementFragment_ editHomeRequirementFragment_;
    private EditBussinessRequirementFragment_ editBussinessRequirementFragment_;

    protected String status;//当前页面的状态，家装还是商装
    private RequirementInfo requirementInfo;

    @AfterViews
    protected void initMainView(){
        mainHeadView.setMianTitle(getString(R.string.update_requirement));
        mainHeadView.setLayoutBackground(R.color.head_layout_bg);
        mainHeadView.setRightTitle(getString(R.string.finish));
        mainHeadView.setRigthTitleEnable(false);

        Intent intent = getIntent();
        requirementInfo = (RequirementInfo)intent.getSerializableExtra(Global.REQUIREMENT_INFO);
        status = requirementInfo.getDec_type();
        initFragment();
    }

    protected void initFragment(){
        FragmentTransaction transaction = this.getSupportFragmentManager()
                .beginTransaction();
        switch (requirementInfo.getDec_type()){
            case Global.DEC_TYPE_BUSINESS:
                editBussinessRequirementFragment_ = new EditBussinessRequirementFragment_();
                editBussinessRequirementFragment_.setArguments(getBundle());
                transaction.replace(R.id.content_layout,editBussinessRequirementFragment_);
                break;
            case Global.DEC_TYPE_HOME:
                editHomeRequirementFragment_ = new EditHomeRequirementFragment_();
                editHomeRequirementFragment_.setArguments(getBundle());
                transaction.replace(R.id.content_layout, editHomeRequirementFragment_);
                break;
        }
        transaction.commit();
    }

    private void resetRightTitleStatus(){
        switch (status){
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
        RequirementInfo requirementInfo = null;
        switch (status){
            case Global.DEC_TYPE_HOME:
                requirementInfo = editHomeRequirementFragment_.getRequirementInfo();
                break;
            case Global.DEC_TYPE_BUSINESS:
                requirementInfo = editBussinessRequirementFragment_.getRequirementInfo();
                break;
        }
        JianFanJiaClient.update_Requirement(this, requirementInfo, this, this);
    }

    @Override
    public void loadSuccess(Object data) {
        super.loadSuccess(data);
        setResult(Activity.RESULT_OK);
        finish();
    }

    @Click({R.id.head_back_layout,R.id.head_right_title})
    protected void click(View view){
        switch (view.getId()){
            case R.id.head_back_layout:
                finish();
                break;
            case R.id.head_right_title:
                confirm();
                break;
        }
    }

    protected Bundle getBundle(){
        Bundle bundle = new Bundle();
        bundle.putSerializable(Global.REQUIREMENT_INFO, requirementInfo);
        bundle.putInt(Global.REQUIREMENG_ACTION_TYPE, XuQiuFragment.REQUESTCODE_EDIT_REQUIREMENT);
        return bundle;
    }

}

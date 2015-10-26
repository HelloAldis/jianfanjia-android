package com.jianfanjia.cn.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.MainHeadView;

/**
 * Description:合同查看
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class ContractActivity extends BaseActivity implements OnClickListener {
    private static final String TAG = ContractActivity.class.getName();
    private MainHeadView mainHeadView = null;

    private String requirementid = null;
    private String final_planid = null;


    @Override
    public void initView() {
        initMainHeadView();
        Intent intent = this.getIntent();
        Bundle contractBundle = intent.getExtras();
        requirementid = contractBundle.getString(Global.REQUIREMENT_ID);
        LogTool.d(TAG, "requirementid:" + requirementid);
        getContractInfo(requirementid);
    }

    private void initMainHeadView() {
        mainHeadView = (MainHeadView) findViewById(R.id.my_contract_head_layout);
        mainHeadView.setBackListener(this);
        mainHeadView.setRightTextListener(this);
        mainHeadView
                .setMianTitle(getResources().getString(R.string.contractText));
        mainHeadView.setRightTitle(getResources().getString(R.string.comfirmText));
        mainHeadView.setLayoutBackground(R.color.head_layout_bg);
        mainHeadView.setRightTitleVisable(View.VISIBLE);
        mainHeadView.setBackLayoutVisable(View.VISIBLE);
    }


    @Override
    public void setListener() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_back_layout:
                finish();
                break;
            case R.id.head_right_title:
                postUserProcess("", "");
                break;
            default:
                break;
        }
    }

    //查看合同
    private void getContractInfo(String requirementid) {
        JianFanJiaClient.getContractInfo(ContractActivity.this, requirementid, getContractListener, this);
    }

    private ApiUiUpdateListener getContractListener = new ApiUiUpdateListener() {
        @Override
        public void preLoad() {

        }

        @Override
        public void loadSuccess(Object data) {
            LogTool.d(TAG, "data:" + data.toString());
        }

        @Override
        public void loadFailture(String error_msg) {

        }
    };

    //确认开启工地
    private void postUserProcess(String requirementid, String final_planid) {
        JianFanJiaClient.post_Owner_Process(ContractActivity.this, requirementid, final_planid, postUserProcessListener, this);
    }


    private ApiUiUpdateListener postUserProcessListener = new ApiUiUpdateListener() {
        @Override
        public void preLoad() {

        }

        @Override
        public void loadSuccess(Object data) {
            LogTool.d(TAG, "data:" + data.toString());
            makeTextLong(data.toString());
        }

        @Override
        public void loadFailture(String error_msg) {

        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_contract;
    }
}

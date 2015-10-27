package com.jianfanjia.cn.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.jianfanjia.cn.adapter.PlanViewAdapter;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.PlandetailInfo;
import com.jianfanjia.cn.bean.RequirementInfo;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.MainHeadView;

import java.util.List;

/**
 * Description:预览设计师方案
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class PreviewDesignerPlanActivity extends BaseActivity implements OnClickListener {
    private static final String TAG = PreviewDesignerPlanActivity.class.getName();
    private MainHeadView mainHeadView = null;
    private ViewPager viewPager = null;
    private TextView cellName = null;
    private TextView houseType = null;
    private TextView houseArea = null;
    private TextView decorateType = null;
    private TextView totalDate = null;
    private TextView price = null;
    private TextView designText = null;

    private Button btnDetail = null;
    private Button btn_choose = null;
    private PlandetailInfo planDetailInfo = null;
    private String designerid = null;
    private String requirementid = null;
    private String planid = null;

    @Override
    public void initView() {
        initMainHeadView();
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        cellName = (TextView) findViewById(R.id.cellName);
        houseType = (TextView) findViewById(R.id.houseType);
        houseArea = (TextView) findViewById(R.id.houseArea);
        decorateType = (TextView) findViewById(R.id.decorateType);
        totalDate = (TextView) findViewById(R.id.totalDate);
        price = (TextView) findViewById(R.id.price);
        designText = (TextView) findViewById(R.id.designText);
        btnDetail = (Button) findViewById(R.id.btnDetail);
        btn_choose = (Button) findViewById(R.id.btn_choose);
        Intent intent = this.getIntent();
        Bundle planBundle = intent.getExtras();
        planid = planBundle.getString(Global.PLAN_ID);
        LogTool.d(TAG, "planid=" + planid);
        getPlanInfo(planid);
    }

    private void initMainHeadView() {
        mainHeadView = (MainHeadView) findViewById(R.id.my_prieview_head_layout);
        mainHeadView.setBackListener(this);
        mainHeadView.setRightTextListener(this);
        mainHeadView
                .setMianTitle(getResources().getString(R.string.designerPlanText));
        mainHeadView.setRightTitle(getResources().getString(R.string.detailPrice));
        mainHeadView.setLayoutBackground(R.color.head_layout_bg);
        mainHeadView.setRightTitleVisable(View.VISIBLE);
        mainHeadView.setBackLayoutVisable(View.VISIBLE);
    }

    @Override
    public void setListener() {
        btnDetail.setOnClickListener(this);
        btn_choose.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_back_layout:
                finish();
                break;
            case R.id.head_right_title:
                startToActivity(planDetailInfo);
                break;
            case R.id.btnDetail:
                startToActivity(planDetailInfo);
                break;
            case R.id.btn_choose:
                chooseDesignerPlan(requirementid, designerid, planid);
                break;
            default:
                break;
        }
    }

    private void startToActivity(PlandetailInfo detailInfo) {
        Bundle priceBundle = new Bundle();
        priceBundle.putSerializable(Global.PLAN_DETAIL, detailInfo);
        startActivity(DetailPriceActivity.class, priceBundle);
    }

    //获取某个方案信息
    private void getPlanInfo(String planid) {
        JianFanJiaClient.getPlanInfo(PreviewDesignerPlanActivity.this, planid, getPlanInfoListener, this);
    }


    //选的方案
    private void chooseDesignerPlan(String requirementid, String designerid, String planid) {
        JianFanJiaClient.chooseDesignerPlan(PreviewDesignerPlanActivity.this, requirementid, designerid, planid, chooseDesignerPlanListener, this);
    }


    private ApiUiUpdateListener getPlanInfoListener = new ApiUiUpdateListener() {
        @Override
        public void preLoad() {
            showWaitDialog(R.string.loading);
        }

        @Override
        public void loadSuccess(Object data) {
            LogTool.d(TAG, "data:" + data);
            hideWaitDialog();
            planDetailInfo = JsonParser.jsonToBean(data.toString(), PlandetailInfo.class);
            LogTool.d(TAG, "planDetailInfo:" + planDetailInfo);
            if (null != planDetailInfo) {
                RequirementInfo requirementInfo = planDetailInfo.getRequirement();
                requirementid = planDetailInfo.getRequirementid();
                designerid = planDetailInfo.getDesignerid();
                LogTool.d(TAG, "requirementid:" + requirementid + " designerid:" + designerid + " requirementInfo:" + requirementInfo);
                cellName.setText(requirementInfo.getCell());
                requirementInfo.getHouse_type();
                requirementInfo.getHouse_area();
                requirementInfo.getWork_type();
                requirementInfo.getTotal_price();
                requirementInfo.getFamily_description();

                String planStatus = planDetailInfo.getStatus();
                if (planStatus.equals(Global.PLAN_STATUS5)) {
                    btn_choose.setEnabled(false);
                }
                List<String> imgList = planDetailInfo.getImages();
                PlanViewAdapter adapter = new PlanViewAdapter(PreviewDesignerPlanActivity.this, imgList);
                viewPager.setAdapter(adapter);
            }
        }

        @Override
        public void loadFailture(String error_msg) {
            makeTextLong(error_msg);
            hideWaitDialog();
        }
    };


    private ApiUiUpdateListener chooseDesignerPlanListener = new ApiUiUpdateListener() {
        @Override
        public void preLoad() {
            showWaitDialog(R.string.submiting);
        }

        @Override
        public void loadSuccess(Object data) {
            LogTool.d(TAG, "data:" + data);
            makeTextLong(data.toString());
            hideWaitDialog();
        }

        @Override
        public void loadFailture(String error_msg) {
            makeTextLong(error_msg);
            hideWaitDialog();
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_preview_designer_plan;
    }
}

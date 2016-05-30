package com.jianfanjia.cn.designer.ui.activity.my_info_auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;

import com.jianfanjia.api.model.Designer;
import com.jianfanjia.api.model.Product;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.designer.config.Constant;
import com.jianfanjia.cn.designer.config.Global;
import com.jianfanjia.cn.designer.tools.BusinessCovertUtil;
import com.jianfanjia.cn.designer.tools.IntentUtil;
import com.jianfanjia.cn.designer.ui.activity.common.ChooseItemActivity;
import com.jianfanjia.cn.designer.view.MainHeadView;
import com.jianfanjia.common.tool.LogTool;

import java.util.List;

/**
 * Description: com.jianfanjia.cn.designer.ui.activity.my_info_auth
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-05-18 11:32
 */
public class DesignerReceiveInfoActivity extends BaseSwipeBackActivity {

    @Bind(R.id.receive_business_head_layout)
    MainHeadView mMainHeadView;

    protected String[] arr_lovestyle;
    protected String[] arr_housetype;
    protected String[] arr_worktype;
    private String[] arr_dectype;
    protected String[] arr_designe_fee_range;

    @Bind(R.id.act_edit_req_dectype_content)
    protected TextView act_edit_req_dectype_content;//装修类型

    @Bind(R.id.act_edit_req_housetype_content)
    protected TextView act_edit_req_housetype_content;//户型

    @Bind(R.id.act_edit_req_lovestyle_content)
    protected TextView act_edit_req_lovestyle_content;//风格喜好

    @Bind(R.id.act_edit_req_work_type_content)
    protected TextView act_edit_req_work_type_content;//包工类型

    @Bind(R.id.act_edit_req_district_content)
    protected TextView act_edit_req_district_content;//接单区域

    @Bind(R.id.receive_business_design_fee_content)
    protected TextView receive_business_design_fee_content;//设计费报价

    @Bind(R.id.receive_business_work_fee_content)
    protected TextView receive_business_work_fee_content;//设计费报价

    @Bind(R.id.receive_business_communication_content)
    protected TextView receive_business_communication_content;//习惯沟通方式

    private Designer mDesigner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getDataFromIntent();
        initView();
        initData();
    }

    private void initStringArray() {
        arr_lovestyle = getResources().getStringArray(R.array.arr_decstyle);
        arr_housetype = getResources().getStringArray(R.array.arr_housetype);
        arr_worktype = getResources().getStringArray(R.array.arr_worktype);
        arr_dectype = getResources().getStringArray(R.array.arr_dectype);
        arr_designe_fee_range = getResources().getStringArray(R.array.arr_fee);
    }

    private void initData() {
        LogTool.d(this.getClass().getName(), "initData");
        if (mDesigner != null) {
            setDecStyle();
            setCommunicationStyle();
            setDecType();
            setDesignFeeRange();
            setHouseType();
            setReceiveDistrict();
            setWorkFee();
            setWorkType();
        }
    }

    private void initView() {
        initMainView();
        initStringArray();
    }

    private void setHouseType() {
        List<String> houseTypes = mDesigner.getDec_house_types();
        StringBuilder sb = new StringBuilder();
        for (String houseTyoe : houseTypes) {
            sb.append(BusinessCovertUtil.convertHouseTypeToShow(houseTyoe));
            sb.append(",");
        }
        if(sb.length() > 0){
             act_edit_req_housetype_content.setText(sb.substring(0,sb.length() - 2));
        }
    }

    private void setDecStyle() {
        List<String> decStyles = mDesigner.getDec_styles();
        StringBuilder sb = new StringBuilder();
        for(String decStyle : decStyles){
            sb.append(BusinessCovertUtil.convertDecStyleToShow(decStyle));
            sb.append(",");
        }
        if(sb.length() > 0){
            act_edit_req_lovestyle_content.setText(sb.substring(0,sb.length() - 2));
        }
    }

    private void setWorkType(){
        List<String> workTypes = mDesigner.getWork_types();
        StringBuilder sb = new StringBuilder();
        for(String workType : workTypes){
            sb.append(BusinessCovertUtil.convertWorktypeToShow(workType));
            sb.append(",");
        }
        if(sb.length() > 0){
            act_edit_req_work_type_content.setText(sb.substring(0,sb.length() - 2));
        }
    }

    private void setDecType(){
        List<String> decTypes = mDesigner.getDec_types();
        StringBuilder sb = new StringBuilder();
        for (String decType : decTypes){
            sb.append(BusinessCovertUtil.convertDectypeToShow(decType));
            sb.append(",");
        }
        if(sb.length() > 0){
            act_edit_req_dectype_content.setText(sb.substring(0, sb.length() - 2));
        }
    }

    private void setDesignFeeRange(){
        if(!TextUtils.isEmpty(mDesigner.getDesign_fee_range())){
            receive_business_design_fee_content.setText(mDesigner.getDesign_fee_range());
        }
    }

    private void setWorkFee(){
//        if(mDesigner.getw)
    }

    private void setReceiveDistrict(){

    }

    private void setCommunicationStyle(){
        if(!TextUtils.isEmpty(mDesigner.getCommunication_type())){
//            receive_business_communication_content.setText();
        }
    }

    private void initMainView() {
        mMainHeadView.setMianTitle(getString(R.string.receive_business_info));
        mMainHeadView.setRightTitle(getString(R.string.commit));
        mMainHeadView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                updateDesignerTeamInfo(mTeam);
            }
        });
//        setMianHeadRightTitleEnable();
    }

    @OnClick({R.id.act_edit_req_dectype, R.id.act_edit_req_work_type, R.id.act_edit_req_lovestyle, R.id.act_edit_req_district,
            R.id.receive_business_housetype_layout, R.id.receive_business_design_fee_layout, R.id.receive_business_work_fee_layout,
            R.id.receive_business_communication_style_layout})
    protected void click(View view) {
        switch (view.getId()) {
            case R.id.act_edit_req_dectype:
                Bundle personBundle = new Bundle();
                personBundle.putInt(Global.REQUIRE_DATA, Constant.REQUIRECODE_DECTYPE);
                personBundle.putInt(ChooseItemActivity.CURRENT_CHOOSED_TYPE, ChooseItemActivity.CHOOSE_TYPE_MULTIPLE);
                IntentUtil.startActivityForResult(this, ChooseItemActivity.class, personBundle, Constant.REQUIRECODE_DECTYPE);
                break;
            case R.id.act_edit_req_work_type:
                break;
            case R.id.act_edit_req_lovestyle:
                break;
            case R.id.act_edit_req_district:
                break;
            case R.id.receive_business_housetype_layout:
                break;
            case R.id.receive_business_design_fee_layout:
                break;
            case R.id.receive_business_work_fee_layout:
                break;
            case R.id.receive_business_communication_style_layout:
                break;
        }
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            mDesigner = (Designer) bundle.getSerializable(Global.DESIGNER_INFO);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_designer_receive_business;
    }
}

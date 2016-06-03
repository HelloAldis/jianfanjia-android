package com.jianfanjia.cn.designer.ui.activity.my_info_auth.receive_business_info;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.model.Designer;
import com.jianfanjia.api.request.designer.UpdateDesignerReceiveInfoRequest;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.api.Api;
import com.jianfanjia.cn.designer.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.designer.config.Constant;
import com.jianfanjia.cn.designer.config.Global;
import com.jianfanjia.cn.designer.tools.BusinessCovertUtil;
import com.jianfanjia.cn.designer.tools.IntentUtil;
import com.jianfanjia.cn.designer.ui.activity.common.choose_item.ChooseItemIntent;
import com.jianfanjia.cn.designer.ui.activity.common.choose_item.ChooseItemLoveStyleIntent;
import com.jianfanjia.cn.designer.ui.interf.cutom_annotation.ReqItemFinderImp;
import com.jianfanjia.cn.designer.view.MainHeadView;
import com.jianfanjia.common.tool.LogTool;

/**
 * Description: com.jianfanjia.cn.designer.ui.activity.my_info_auth
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-05-18 11:32
 */
public class DesignerReceiveInfoActivity extends BaseSwipeBackActivity {

    @Bind(R.id.receive_business_head_layout)
    MainHeadView mMainHeadView;

    protected String[] arr_district;

    @Bind(R.id.act_edit_req_dectype_content)
    protected TextView act_edit_req_dectype_content;//装修类型

    @Bind(R.id.receive_business_housetype_content)
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
        arr_district = getResources().getStringArray(R.array.arr_district);
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
            sb.append(" ");
        }
        if (sb.length() > 0) {
            if (sb.length() > 12) {
                act_edit_req_housetype_content.setText(getString(R.string.click_view));
                return;
            }
            act_edit_req_housetype_content.setText(sb.substring(0, sb.length() - 1));
        }else {
            act_edit_req_housetype_content.setText(null);
        }
    }

    private void setDecStyle() {
        List<String> decStyles = mDesigner.getDec_styles();
        StringBuilder sb = new StringBuilder();
        for (String decStyle : decStyles) {
            sb.append(BusinessCovertUtil.convertDecStyleToShow(decStyle));
            sb.append(" ");
        }
        if (sb.length() > 0) {
            act_edit_req_lovestyle_content.setText(sb.substring(0, sb.length() - 1));
        }else{
            act_edit_req_lovestyle_content.setText(null);
        }
    }

    private void setWorkType() {
        List<String> workTypes = mDesigner.getWork_types();
        StringBuilder sb = new StringBuilder();
        for (String workType : workTypes) {
            sb.append(BusinessCovertUtil.convertWorktypeToShow(workType));
            sb.append(" ");
        }
        if (sb.length() > 0) {
            act_edit_req_work_type_content.setText(sb.substring(0, sb.length() - 1));
        }else{
            act_edit_req_work_type_content.setText(null);
        }
    }

    private void setDecType() {
        List<String> decTypes = mDesigner.getDec_types();
        StringBuilder sb = new StringBuilder();
        for (String decType : decTypes) {
            sb.append(BusinessCovertUtil.convertDectypeToShow(decType));
            sb.append(" ");
        }
        if (sb.length() > 0) {
            act_edit_req_dectype_content.setText(sb.substring(0, sb.length() - 1));
        }else {
            act_edit_req_dectype_content.setText(null);
        }
    }

    private void setDesignFeeRange() {
        if (!TextUtils.isEmpty(mDesigner.getDesign_fee_range())) {
            receive_business_design_fee_content.setText(BusinessCovertUtil.convertDesignFeeToShow(mDesigner
                    .getDesign_fee_range()));
        }else {
            receive_business_design_fee_content.setText(null);
        }
    }

    private void setWorkFee() {
        receive_business_work_fee_content.setText(mDesigner.getDec_fee_half() + " / " + mDesigner.getDec_fee_all());
    }

    private void setReceiveDistrict() {
        List<String> receiveDistricts = mDesigner.getDec_districts();
        StringBuilder sb = new StringBuilder();
        for (String district : receiveDistricts) {
            sb.append(district);
            sb.append(" ");
        }
        if (sb.length() > 0) {
            if (sb.length() > 12) {
                act_edit_req_district_content.setText(getString(R.string.click_view));
                return;
            }
            act_edit_req_district_content.setText(sb.substring(0, sb.length() - 1));
        }else {
            act_edit_req_district_content.setText(null);
        }
    }

    private void setCommunicationStyle() {
        if (!TextUtils.isEmpty(mDesigner.getCommunication_type())) {
            receive_business_communication_content.setText(BusinessCovertUtil.convertCommunicationStyleToShow
                    (mDesigner.getCommunication_type()));
        }else{
            receive_business_communication_content.setText(null);
        }
    }

    private void initMainView() {
        mMainHeadView.setMianTitle(getString(R.string.receive_business_info));
    }

    @OnClick({R.id.head_back_layout, R.id.act_edit_req_dectype, R.id.act_edit_req_work_type, R.id
            .act_edit_req_lovestyle, R.id
            .act_edit_req_district,
            R.id.receive_business_housetype_layout, R.id.receive_business_design_fee_layout, R.id
            .receive_business_work_fee_layout,
            R.id.receive_business_communication_style_layout})
    protected void click(View view) {
        switch (view.getId()) {
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
            case R.id.act_edit_req_dectype:
                ChooseItemIntent decTypeIntent = new ChooseItemIntent(this);
                decTypeIntent.setMultipleChoose(Constant.REQUIRECODE_DECTYPE, mDesigner.getDec_types(), getString(R
                        .string.str_decoratetype));
                startActivityForResult(decTypeIntent, Constant.REQUIRECODE_DECTYPE);
                break;
            case R.id.act_edit_req_work_type:
                ChooseItemIntent workTypeIntent = new ChooseItemIntent(this);
                workTypeIntent.setMultipleChoose(Constant.REQUIRECODE_WORKTYPE, mDesigner.getWork_types(), getString
                        (R.string.str_work_type));
                startActivityForResult(workTypeIntent, Constant.REQUIRECODE_WORKTYPE);
                break;
            case R.id.act_edit_req_lovestyle:
                ChooseItemLoveStyleIntent chooseItemLoveStyleIntent = new ChooseItemLoveStyleIntent(this);
                chooseItemLoveStyleIntent.setMultipleChoose(Constant.REQUIRECODE_LOVESTYLE, mDesigner.getDec_styles()
                        , getString(R.string.str_dec_goodatstyle));
                startActivityForResult(chooseItemLoveStyleIntent, Constant.REQUIRECODE_LOVESTYLE);
                break;
            case R.id.act_edit_req_district:
                ChooseItemIntent districtIntent = new ChooseItemIntent(this);
                List<String> chooseDistrictKey = new ArrayList<>();
                for (String choosedValue : mDesigner.getDec_districts()) {
                    int i = 0;
                    for (String value : arr_district) {
                        if (choosedValue.equals(value)) {
                            chooseDistrictKey.add(i + "");
                            break;
                        }
                        i++;
                    }
                }
                districtIntent.setMultipleChoose(Constant.REQUIRECODE_DISTRICT, chooseDistrictKey, getString(R.string
                        .str_receive_district));
                startActivityForResult(districtIntent, Constant.REQUIRECODE_DISTRICT);
                break;
            case R.id.receive_business_housetype_layout:
                ChooseItemIntent houseTypeIntent = new ChooseItemIntent(this);
                houseTypeIntent.setMultipleChoose(Constant.REQUIRECODE_HOUSETYPE, mDesigner.getDec_house_types(),
                        getString(R.string.str_receive_business_housetype));
                startActivityForResult(houseTypeIntent, Constant.REQUIRECODE_HOUSETYPE);
                break;
            case R.id.receive_business_design_fee_layout:
                ChooseItemIntent designFeeIntent = new ChooseItemIntent(this);
                designFeeIntent.setSingleChoose(Constant.REQUIRECODE_DESIGN_FEE, mDesigner.getDesign_fee_range(),
                        getString(R.string.str_receive_business_design_fee));
                startActivityForResult(designFeeIntent, Constant.REQUIRECODE_DESIGN_FEE);
                break;
            case R.id.receive_business_work_fee_layout:
                Bundle workFeeBundle = new Bundle();
                ChooseWorkFeeActivity.WorkFeeInfo workFeeInfo = new ChooseWorkFeeActivity.WorkFeeInfo();
                workFeeInfo.setWork_fee_half_min(mDesigner.getDec_fee_half());
                workFeeInfo.setWork_fee_all_min(mDesigner.getDec_fee_all());
                workFeeBundle.putSerializable(ChooseWorkFeeActivity.WORK_FEE_INFO, workFeeInfo);
                IntentUtil.startActivityForResult(this, ChooseWorkFeeActivity.class, workFeeBundle, Constant
                        .REQUIRECODE_WORK_FEE);
                break;
            case R.id.receive_business_communication_style_layout:
                ChooseItemIntent communicationIntent = new ChooseItemIntent(this);
                communicationIntent.setSingleChoose(Constant.REQUIRECODE_LOVEDESISTYLE, mDesigner
                        .getCommunication_type(), getString(R.string.receive_business_communication));
                startActivityForResult(communicationIntent, Constant.REQUIRECODE_LOVEDESISTYLE);
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

    private void updateReceiveInfo(Designer designer) {
        UpdateDesignerReceiveInfoRequest updateDesignerReceiveInfoRequest = new UpdateDesignerReceiveInfoRequest();
        updateDesignerReceiveInfoRequest.setDesigner(designer);

        Api.updateDesignerBusinessInfo(updateDesignerReceiveInfoRequest, new ApiCallback<ApiResponse<String>>() {
            @Override
            public void onPreLoad() {

            }

            @Override
            public void onHttpDone() {

            }

            @Override
            public void onSuccess(ApiResponse<String> apiResponse) {
                dataManager.setDesigner(mDesigner);
            }

            @Override
            public void onFailed(ApiResponse<String> apiResponse) {
                makeTextShort(apiResponse.getErr_msg());
            }

            @Override
            public void onNetworkError(int code) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case Constant.REQUIRECODE_DECTYPE:
                List<ReqItemFinderImp.ItemMap> decTypeItemMaps = data.getParcelableArrayListExtra(Global.RESPONSE_DATA);
                mDesigner.getDec_types().clear();
                for (ReqItemFinderImp.ItemMap itemMap : decTypeItemMaps) {
                    LogTool.d(this.getClass().getName(), "itemMap.key =" + itemMap.key);
                    mDesigner.getDec_types().add(itemMap.key);
                }
                setDecType();
                break;
            case Constant.REQUIRECODE_WORKTYPE:
                List<ReqItemFinderImp.ItemMap> workTypeItemMaps = data.getParcelableArrayListExtra(Global
                        .RESPONSE_DATA);
                mDesigner.getWork_types().clear();
                for (ReqItemFinderImp.ItemMap itemMap : workTypeItemMaps) {
                    LogTool.d(this.getClass().getName(), "itemMap.key =" + itemMap.key);
                    mDesigner.getWork_types().add(itemMap.key);
                }
                setWorkType();
                break;
            case Constant.REQUIRECODE_DISTRICT:
                List<ReqItemFinderImp.ItemMap> decDistrictItemMaps = data.getParcelableArrayListExtra(Global
                        .RESPONSE_DATA);
                mDesigner.getDec_districts().clear();
                for (ReqItemFinderImp.ItemMap itemMap : decDistrictItemMaps) {
                    LogTool.d(this.getClass().getName(), "itemMap.key =" + itemMap.key);
                    mDesigner.getDec_districts().add(itemMap.value);
                }
                setReceiveDistrict();
                break;
            case Constant.REQUIRECODE_HOUSETYPE:
                List<ReqItemFinderImp.ItemMap> houseTypeItemMaps = data.getParcelableArrayListExtra(Global
                        .RESPONSE_DATA);
                mDesigner.getDec_house_types().clear();
                for (ReqItemFinderImp.ItemMap itemMap : houseTypeItemMaps) {
                    LogTool.d(this.getClass().getName(), "itemMap.key =" + itemMap.key);
                    mDesigner.getDec_house_types().add(itemMap.key);
                }
                setHouseType();
                break;
            case Constant.REQUIRECODE_DESIGN_FEE:
                ReqItemFinderImp.ItemMap designFeeItemMaps = data.getParcelableExtra(Global.RESPONSE_DATA);
                if (designFeeItemMaps != null) {
                    mDesigner.setDesign_fee_range(designFeeItemMaps.key);
                }
                setDesignFeeRange();
                break;
            case Constant.REQUIRECODE_LOVEDESISTYLE:
                ReqItemFinderImp.ItemMap communicationItemMaps = data.getParcelableExtra(Global.RESPONSE_DATA);
                if (communicationItemMaps != null) {
                    mDesigner.setCommunication_type(communicationItemMaps.key);
                }
                setCommunicationStyle();
                break;
            case Constant.REQUIRECODE_LOVESTYLE:
                List<ReqItemFinderImp.ItemMap> loveStyleItemMaps = data.getParcelableArrayListExtra(Global
                        .RESPONSE_DATA);
                mDesigner.getDec_styles().clear();
                for (ReqItemFinderImp.ItemMap itemMap : loveStyleItemMaps) {
                    LogTool.d(this.getClass().getName(), "itemMap.key =" + itemMap.key);
                    mDesigner.getDec_styles().add(itemMap.key);
                }
                setDecStyle();
                break;
            case Constant.REQUIRECODE_WORK_FEE:
                ChooseWorkFeeActivity.WorkFeeInfo workFeeInfo = (ChooseWorkFeeActivity.WorkFeeInfo) data
                        .getSerializableExtra(ChooseWorkFeeActivity.WORK_FEE_INFO);
                if (workFeeInfo != null) {
                    mDesigner.setDec_fee_half(workFeeInfo.getWork_fee_half_min());
                    mDesigner.setDec_fee_all(workFeeInfo.getWork_fee_all_min());
                }
                setWorkFee();
                break;
        }
        updateReceiveInfo(mDesigner);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_designer_receive_business;
    }
}

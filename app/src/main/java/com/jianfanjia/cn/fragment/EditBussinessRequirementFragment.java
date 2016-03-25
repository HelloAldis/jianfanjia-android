package com.jianfanjia.cn.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.my.EditCityActivity;
import com.jianfanjia.cn.activity.requirement.EditRequirementItemActivity;
import com.jianfanjia.cn.activity.requirement.EditRequirementLovestyleActivity;
import com.jianfanjia.cn.base.BaseAnnotationFragment;
import com.jianfanjia.cn.bean.RequirementInfo;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.interf.NotifyActivityStatusChange;
import com.jianfanjia.cn.interf.cutom_annotation.ReqItemFinderImp;
import com.jianfanjia.cn.tools.LogTool;

/**
 * Description: com.jianfanjia.cn.fragment
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-12-15 14:43
 */
public class EditBussinessRequirementFragment extends BaseAnnotationFragment {

    private NotifyActivityStatusChange hostActivity;
    protected boolean isFinish = false;//编辑是否已经完成
    private int actionType = -1;//创建需求or修改需求

    @Bind(R.id.act_edit_req_city_content)
    protected TextView act_edit_req_city_content;//所在城市
    @Bind(R.id.act_edit_req_lovestyle_content)
    protected TextView act_edit_req_lovestyle_content;//风格喜好
    @Bind(R.id.act_edit_req_lovedesistyle_content)
    protected TextView act_edit_req_lovedesistyle_content;//偏好设计师类型
    @Bind(R.id.act_edit_req_decoratetype_content)
    protected TextView act_edit_req_decoratetype_content;//装修类型
    @Bind(R.id.act_edit_req_work_type_content)
    protected TextView act_edit_req_work_type_content;//包工类型
    @Bind(R.id.act_edit_req_lovedesisex_content)
    protected TextView act_edit_req_lovedesisex_content;//偏好设计师性别
    @Bind(R.id.act_edit_req_street_content)
    protected TextView act_edit_req_street_content;//所在街道
    @Bind(R.id.act_edit_req_housearea_content)
    protected TextView act_edit_req_housearea_content;//装修面积
    @Bind(R.id.act_edit_req_decoratebudget_content)
    protected TextView act_edit_req_decoratebudget_content;//装修预算
    @Bind(R.id.act_edit_req_cell_content)
    protected TextView act_edit_req_cell_content;//小区

    protected String[] arr_lovestyle;
    protected String[] arr_housetype;
    protected String[] arr_love_designerstyle;
    protected String[] arr_worktype;
    protected String[] arr_busihousetype;
    protected String[] arr_desisex;

    private RequirementInfo requirementInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  super.onCreateView(inflater, container, savedInstanceState);
        initView();
        initData();
        return view;
    }

    public void initView(){
        initStringArray();
    }

    private void initStringArray() {
        arr_lovestyle = getResources().getStringArray(R.array.arr_decstyle);
        arr_housetype = getResources().getStringArray(R.array.arr_housetype);
        arr_love_designerstyle = getResources().getStringArray(R.array.arr_love_designerstyle);
        arr_worktype = getResources().getStringArray(R.array.arr_worktype);
        arr_busihousetype = getResources().getStringArray(R.array.arr_busi_housetype);
        arr_desisex = getResources().getStringArray(R.array.arr_desisex);
    }

    @OnTextChanged({R.id.act_edit_req_cell_content, R.id.act_edit_req_street_content, R.id.act_edit_req_housearea_content, R.id.act_edit_req_decoratebudget_content})
    protected void afterTextChangedOnSomeTextViews(TextView tv, Editable text) {
        int viewId = tv.getId();
        String textContent = text.toString();
        LogTool.d(getClass().getName() + "afterchange ", viewId + text.toString());
        switch (viewId) {
            case R.id.act_edit_req_street_content:
                requirementInfo.setStreet(textContent);
                break;
            case R.id.act_edit_req_housearea_content:
                requirementInfo.setHouse_area(textContent);
                break;
            case R.id.act_edit_req_decoratebudget_content:
                requirementInfo.setTotal_price(textContent);
                break;
            case R.id.act_edit_req_cell_content:
                requirementInfo.setCell(textContent);
                break;
        }
        isAllInput();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(getActivity() instanceof NotifyActivityStatusChange)) {
            throw new IllegalArgumentException("hostactivity not implements NotifyActivityStatusChange");
        }
        hostActivity = (NotifyActivityStatusChange) getActivity();
    }

    /**
     * 是否所有的字段都已经输入
     *
     * @return
     */
    public void isAllInput() {
        if (act_edit_req_city_content.length() > 0
                && act_edit_req_cell_content.length() > 0
                && act_edit_req_decoratebudget_content.length() > 0
                && act_edit_req_decoratetype_content.length() > 0
                && act_edit_req_housearea_content.length() > 0
                && act_edit_req_lovedesisex_content.length() > 0
                && act_edit_req_lovestyle_content.length() > 0
                && act_edit_req_work_type_content.length() > 0
                && act_edit_req_street_content.length() > 0
                && act_edit_req_lovedesistyle_content.length() > 0) {
            isFinish = true;
        } else {
            isFinish = false;
        }
        LogTool.d(this.getClass().getName(), "isFinish = " + isFinish);
        hostActivity.notifyStatusChange();
    }

    public boolean isFinish() {
        return isFinish;
    }

    @OnClick({R.id.act_edit_req_city, R.id.act_edit_req_decoratetype,
            R.id.act_edit_req_lovestyle,
            R.id.act_edit_req_persons,
            R.id.act_edit_req_lovedesistyle,
            R.id.act_edit_req_lovedesisex,
            R.id.act_edit_req_work_type})
    protected void back(View clickView) {
        int viewId = clickView.getId();
        switch (viewId) {
            case R.id.act_edit_req_city:
                Bundle address = new Bundle();
                address.putString(Constant.EDIT_PROVICE, requirementInfo.getProvince());
                address.putString(Constant.EDIT_CITY, requirementInfo.getCity());
                address.putString(Constant.EDIT_DISTRICT, requirementInfo.getDistrict());
                address.putInt(EditCityActivity.PAGE, EditCityActivity.EDIT_REQUIREMENT_ADRESS);
                startActivityForResult(EditCityActivity.class, address, Constant.REQUIRECODE_CITY);
                break;
            case R.id.act_edit_req_lovedesistyle:
                Bundle loveDesignerBundle = new Bundle();
                loveDesignerBundle.putInt(Global.REQUIRE_DATA, Constant.REQUIRECODE_LOVEDESISTYLE);
                startActivityForResult(EditRequirementItemActivity.class, loveDesignerBundle, Constant.REQUIRECODE_LOVEDESISTYLE);
                break;
            case R.id.act_edit_req_lovestyle:
                Bundle loveStyleBundle = new Bundle();
                loveStyleBundle.putInt(Global.REQUIRE_DATA, Constant.REQUIRECODE_LOVESTYLE);
                startActivityForResult(EditRequirementLovestyleActivity.class, loveStyleBundle, Constant.REQUIRECODE_LOVESTYLE);
                break;
            case R.id.act_edit_req_decoratetype:
                Bundle decorateTypeBundle = new Bundle();
                decorateTypeBundle.putInt(Global.REQUIRE_DATA, Constant.REQUIRECODE_BUSI_DECORATETYPE);
                startActivityForResult(EditRequirementItemActivity.class, decorateTypeBundle, Constant.REQUIRECODE_BUSI_DECORATETYPE);
                break;
            case R.id.act_edit_req_work_type:
                Bundle workTypeBundle = new Bundle();
                workTypeBundle.putInt(Global.REQUIRE_DATA, Constant.REQUIRECODE_WORKTYPE);
                startActivityForResult(EditRequirementItemActivity.class,workTypeBundle, Constant.REQUIRECODE_WORKTYPE);
                break;
            case R.id.act_edit_req_lovedesisex:
                Bundle loveDesiSexBundle = new Bundle();
                loveDesiSexBundle.putInt(Global.REQUIRE_DATA, Constant.REQUIRECODE_DESISEX);
                startActivityForResult(EditRequirementItemActivity.class,loveDesiSexBundle, Constant.REQUIRECODE_DESISEX);
                break;
            default:
                break;
        }
    }

    private void initData() {
        LogTool.d(this.getClass().getName(), "initData");
        requirementInfo = (RequirementInfo) getArguments().getSerializable(Global.REQUIREMENT_INFO);
        actionType = getArguments().getInt(Global.REQUIREMENG_ACTION_TYPE, 0);
        switch (actionType) {
            case XuQiuFragment.REQUESTCODE_EDIT_REQUIREMENT:
            case XuQiuFragment.REQUESTCODE_PUBLISH_REQUIREMENT:
                if (!TextUtils.isEmpty(requirementInfo.getProvince()) && !TextUtils.isEmpty(requirementInfo.getCity()) && !TextUtils.isEmpty(requirementInfo.getDistrict())) {
                    act_edit_req_city_content.setText(requirementInfo.getProvince() + requirementInfo.getCity() + requirementInfo.getDistrict());
                }
                if (!TextUtils.isEmpty(requirementInfo.getStreet())) {
                    act_edit_req_street_content.setText(requirementInfo.getStreet());
                }
                if (!TextUtils.isEmpty(requirementInfo.getCell())) {
                    act_edit_req_cell_content.setText(requirementInfo.getCell());
                }
                if (!TextUtils.isEmpty(requirementInfo.getHouse_area())) {
                    act_edit_req_housearea_content.setText(requirementInfo.getHouse_area());
                }
                if (!TextUtils.isEmpty(requirementInfo.getTotal_price())) {
                    act_edit_req_decoratebudget_content.setText(requirementInfo.getTotal_price());
                }
                act_edit_req_decoratetype_content.setText(TextUtils.isEmpty(requirementInfo.getBusiness_house_type()) ? "" : arr_busihousetype[Integer.parseInt(requirementInfo.getBusiness_house_type()) > (arr_busihousetype.length - 1) ? (arr_busihousetype.length - 1) : Integer.parseInt(requirementInfo.getBusiness_house_type())]);
                act_edit_req_lovestyle_content.setText(TextUtils.isEmpty(requirementInfo.getDec_style()) ? "" : arr_lovestyle[Integer.parseInt(requirementInfo.getDec_style())]);
                act_edit_req_lovedesistyle_content.setText(TextUtils.isEmpty(requirementInfo.getCommunication_type()) ? "" : arr_love_designerstyle[Integer.parseInt(requirementInfo.getCommunication_type())]);
                act_edit_req_lovedesisex_content.setText(TextUtils.isEmpty(requirementInfo.getPrefer_sex()) ? "" : arr_desisex[Integer.parseInt(requirementInfo.getPrefer_sex())]);
                act_edit_req_work_type_content.setText(TextUtils.isEmpty(requirementInfo.getWork_type()) ? "" : arr_worktype[Integer.parseInt(requirementInfo.getWork_type())]);
                break;
        }

    }

    public RequirementInfo getRequirementInfo() {
        return requirementInfo;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (data != null) {
            ReqItemFinderImp.ItemMap itemMap = (ReqItemFinderImp.ItemMap) data.getSerializableExtra(Global.RESPONSE_DATA);
            switch (requestCode) {
                case Constant.REQUIRECODE_CITY:
                    String provice = data.getStringExtra(Constant.EDIT_PROVICE);
                    String city = data.getStringExtra(Constant.EDIT_CITY);
                    String district = data.getStringExtra(Constant.EDIT_DISTRICT);
                    if (!TextUtils.isEmpty(provice) && !TextUtils.isEmpty(city) && !TextUtils.isEmpty(district)) {
                        act_edit_req_city_content.setText(provice + city + district);
                        requirementInfo.setProvince(provice);
                        requirementInfo.setCity(city);
                        requirementInfo.setDistrict(district);
                    }
                    break;
                case Constant.REQUIRECODE_LOVEDESISTYLE:
                    act_edit_req_lovedesistyle_content.setText(itemMap.value);
                    requirementInfo.setCommunication_type(itemMap.key);
                    break;
                case Constant.REQUIRECODE_LOVESTYLE:
                    act_edit_req_lovestyle_content.setText(itemMap.value);
                    requirementInfo.setDec_style(itemMap.key);
                    break;
                case Constant.REQUIRECODE_BUSI_DECORATETYPE:
                    act_edit_req_decoratetype_content.setText(itemMap.value);
                    requirementInfo.setBusiness_house_type(itemMap.key);
                    break;
                case Constant.REQUIRECODE_WORKTYPE:
                    act_edit_req_work_type_content.setText(arr_worktype[Integer.parseInt(itemMap.key)]);
                    requirementInfo.setWork_type(itemMap.key);
                    break;
                case Constant.REQUIRECODE_DESISEX:
                    act_edit_req_lovedesisex_content.setText(itemMap.value);
                    requirementInfo.setPrefer_sex(itemMap.key);
                    break;
            }
            isAllInput();
        }

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_edit_bussiness_req;
    }
}

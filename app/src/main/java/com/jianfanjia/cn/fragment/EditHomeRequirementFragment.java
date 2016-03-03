package com.jianfanjia.cn.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.my.EditCityActivity;
import com.jianfanjia.cn.activity.my.EditCityActivity_;
import com.jianfanjia.cn.activity.requirement.EditRequirementItemActivity_;
import com.jianfanjia.cn.activity.requirement.EditRequirementLovestyleActivity_;
import com.jianfanjia.cn.base.BaseAnnotationFragment;
import com.jianfanjia.cn.bean.RequirementInfo;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.interf.NotifyActivityStatusChange;
import com.jianfanjia.cn.interf.cutom_annotation.ReqItemFinderImp;
import com.jianfanjia.cn.tools.LogTool;

import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringArrayRes;

/**
 * Description: com.jianfanjia.cn.fragment
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-12-15 14:43
 */
@EFragment(R.layout.fragment_edt_home_req)
public class EditHomeRequirementFragment extends BaseAnnotationFragment {

    private NotifyActivityStatusChange hostActivity;
    private boolean isFinish = false;//是否所有字段都填了
    private int actionType = -1;//创建需求or修改需求

    @ViewById
    protected TextView act_edit_req_city_content;//所在城市
    @ViewById
    protected TextView act_edit_req_housetype_content;//户型
    @ViewById
    protected TextView act_edit_req_persons_content;//计划常住人口
    @ViewById
    protected TextView act_edit_req_lovestyle_content;//风格喜好
    @ViewById
    protected TextView act_edit_req_lovedesistyle_content;//偏好设计师类型
    @ViewById
    protected TextView act_edit_req_work_type_content;//包工类型
    @ViewById
    protected TextView act_edit_req_lovedesisex_content;//偏好设计师性别
    @ViewById
    protected EditText act_edit_req_housearea_content;//装修面积
    @ViewById
    protected EditText act_edit_req_decoratebudget_content;//装修预算
    @ViewById
    protected EditText act_edit_req_cell_content;//小区
    @ViewById
    protected EditText act_edit_req_qi_content;//期
    @ViewById
    protected EditText act_edit_req_danyuan_content;//单元
    @ViewById
    protected EditText act_edit_req_dong_content;//栋
    @ViewById
    protected EditText act_edit_req_shi_content;//室

    @StringArrayRes(R.array.arr_decstyle)
    protected String[] arr_lovestyle;
    @StringArrayRes(R.array.arr_housetype)
    protected String[] arr_housetype;
    @StringArrayRes(R.array.arr_love_designerstyle)
    protected String[] arr_love_designerstyle;
    @StringArrayRes(R.array.arr_worktype)
    protected String[] arr_worktype;
    @StringArrayRes(R.array.arr_desisex)
    protected String[] arr_desisex;

    private RequirementInfo requirementInfo;

    @AfterTextChange({R.id.act_edit_req_cell_content, R.id.act_edit_req_qi_content, R.id.act_edit_req_danyuan_content, R.id.act_edit_req_dong_content,
            R.id.act_edit_req_shi_content, R.id.act_edit_req_housearea_content, R.id.act_edit_req_decoratebudget_content})
    protected void afterTextChangedOnSomeTextViews(TextView tv, Editable text) {
        int viewId = tv.getId();
        String textContent = text.toString();
        LogTool.d(getClass().getName() + "afterchange ", viewId + text.toString());
        switch (viewId) {
            case R.id.act_edit_req_cell_content:
                requirementInfo.setCell(textContent);
                break;
            case R.id.act_edit_req_qi_content:
                requirementInfo.setCell_phase(textContent);
                break;
            case R.id.act_edit_req_danyuan_content:
                requirementInfo.setCell_unit(textContent);
                break;
            case R.id.act_edit_req_dong_content:
                requirementInfo.setCell_building(textContent);
                break;
            case R.id.act_edit_req_shi_content:
                requirementInfo.setCell_detail_number(textContent);
                break;
            case R.id.act_edit_req_housearea_content:
                requirementInfo.setHouse_area(textContent);
                break;
            case R.id.act_edit_req_decoratebudget_content:
                requirementInfo.setTotal_price(textContent);
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

    public boolean isFinish() {
        return isFinish;
    }

    /**
     * 是否所有的字段都已经输入
     *
     * @return
     */
    public void isAllInput() {
        if (act_edit_req_city_content.length() > 0
                && act_edit_req_cell_content.length() > 0
                && act_edit_req_danyuan_content.length() > 0
                && act_edit_req_decoratebudget_content.length() > 0
                && act_edit_req_dong_content.length() > 0
                && act_edit_req_housearea_content.length() > 0
                && act_edit_req_housetype_content.length() > 0
                && act_edit_req_lovedesisex_content.length() > 0
                && act_edit_req_lovestyle_content.length() > 0
                && act_edit_req_persons_content.length() > 0
                && act_edit_req_qi_content.length() > 0
                && act_edit_req_shi_content.length() > 0
                && act_edit_req_dong_content.length() > 0
                && act_edit_req_work_type_content.length() > 0) {
            isFinish = true;
        } else {
            isFinish = false;
        }
        LogTool.d(this.getClass().getName(), "isFinish = " + isFinish);
        hostActivity.notifyStatusChange();
    }

    @Click({R.id.act_edit_req_city, R.id.act_edit_req_housetype,
            R.id.act_edit_req_decoratetype,
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
                startActivityForResult(EditCityActivity_.class, address, Constant.REQUIRECODE_CITY);
                break;
            case R.id.act_edit_req_lovedesistyle:
                Bundle loveDesignerBundle = new Bundle();
                loveDesignerBundle.putInt(Global.REQUIRE_DATA, Constant.REQUIRECODE_LOVEDESISTYLE);
                startActivityForResult(EditRequirementItemActivity_.class, loveDesignerBundle, Constant.REQUIRECODE_LOVEDESISTYLE);
                break;
            case R.id.act_edit_req_lovestyle:
                Bundle loveStyleBundle = new Bundle();
                loveStyleBundle.putInt(Global.REQUIRE_DATA, Constant.REQUIRECODE_LOVESTYLE);
                startActivityForResult(EditRequirementLovestyleActivity_.class, loveStyleBundle, Constant.REQUIRECODE_LOVESTYLE);
                break;
            case R.id.act_edit_req_persons:
                Bundle personBundle = new Bundle();
                personBundle.putInt(Global.REQUIRE_DATA, Constant.REQUIRECODE_PERSONS);
                startActivityForResult(EditRequirementItemActivity_.class, personBundle, Constant.REQUIRECODE_PERSONS);
                break;
            case R.id.act_edit_req_housetype:
                Bundle houseTypeBundle = new Bundle();
                houseTypeBundle.putInt(Global.REQUIRE_DATA, Constant.REQUIRECODE_HOUSETYPE);
                startActivityForResult(EditRequirementItemActivity_.class, houseTypeBundle, Constant.REQUIRECODE_HOUSETYPE);
                break;
            case R.id.act_edit_req_work_type:
                Bundle workTypeBundle = new Bundle();
                workTypeBundle.putInt(Global.REQUIRE_DATA, Constant.REQUIRECODE_WORKTYPE);
                startActivityForResult(EditRequirementItemActivity_.class, workTypeBundle, Constant.REQUIRECODE_WORKTYPE);
                break;
            case R.id.act_edit_req_lovedesisex:
                Bundle lovedesisexBundle = new Bundle();
                lovedesisexBundle.putInt(Global.REQUIRE_DATA, Constant.REQUIRECODE_DESISEX);
                startActivityForResult(EditRequirementItemActivity_.class, lovedesisexBundle, Constant.REQUIRECODE_DESISEX);
                break;
            default:
                break;
        }
    }

    @AfterViews
    protected void setMainHeadView() {

        initData();
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
                if (!TextUtils.isEmpty(requirementInfo.getCell())) {
                    act_edit_req_cell_content.setText(requirementInfo.getCell());
                }
                if (!TextUtils.isEmpty(requirementInfo.getCell_phase())) {
                    act_edit_req_qi_content.setText(requirementInfo.getCell_phase());
                }
                if (!TextUtils.isEmpty(requirementInfo.getCell_unit())) {
                    act_edit_req_danyuan_content.setText(requirementInfo.getCell_unit());
                }
                if (!TextUtils.isEmpty(requirementInfo.getCell_building())) {
                    act_edit_req_dong_content.setText(requirementInfo.getCell_building());
                }
                if (!TextUtils.isEmpty(requirementInfo.getCell_detail_number())) {
                    act_edit_req_shi_content.setText(requirementInfo.getCell_detail_number());
                }
                if (!TextUtils.isEmpty(requirementInfo.getHouse_area())) {
                    act_edit_req_housearea_content.setText(requirementInfo.getHouse_area());
                }
                if (!TextUtils.isEmpty(requirementInfo.getTotal_price())) {
                    act_edit_req_decoratebudget_content.setText(requirementInfo.getTotal_price());
                }
                act_edit_req_housetype_content.setText(TextUtils.isEmpty(requirementInfo.getHouse_type()) ? "" : arr_housetype[Integer.parseInt(requirementInfo.getHouse_type())]);
                act_edit_req_persons_content.setText(TextUtils.isEmpty(requirementInfo.getFamily_description()) ? "" : requirementInfo.getFamily_description());
                act_edit_req_lovestyle_content.setText(TextUtils.isEmpty(requirementInfo.getDec_style()) ? "" : arr_lovestyle[Integer.parseInt(requirementInfo.getDec_style())]);
                act_edit_req_lovedesistyle_content.setText(TextUtils.isEmpty(requirementInfo.getCommunication_type()) ? "" : arr_love_designerstyle[Integer.parseInt(requirementInfo.getCommunication_type())]);
                act_edit_req_lovedesisex_content.setText(TextUtils.isEmpty(requirementInfo.getPrefer_sex()) ? "" : arr_desisex[Integer.parseInt(requirementInfo.getPrefer_sex())]);
                act_edit_req_work_type_content.setText(TextUtils.isEmpty(requirementInfo.getWork_type()) ? "" : arr_worktype[Integer.parseInt(requirementInfo.getWork_type())]);
                break;
                /*act_edit_req_persons_content.setText(TextUtils.isEmpty(requirementInfo.getFamily_description()) ? "" : requirementInfo.getFamily_description());
                act_edit_req_housetype_content.setText(TextUtils.isEmpty(requirementInfo.getHouse_type()) ? "" : arr_housetype[Integer.parseInt(requirementInfo.getHouse_type())]);
                act_edit_req_persons_content.setText(TextUtils.isEmpty(requirementInfo.getFamily_description()) ? "" : requirementInfo.getFamily_description());
                act_edit_req_lovestyle_content.setText(TextUtils.isEmpty(requirementInfo.getDec_style()) ? "" : arr_lovestyle[Integer.parseInt(requirementInfo.getDec_style())]);
                act_edit_req_lovedesistyle_content.setText(TextUtils.isEmpty(requirementInfo.getCommunication_type()) ? "" : arr_love_designerstyle[Integer.parseInt(requirementInfo.getCommunication_type())]);
                act_edit_req_lovedesisex_content.setText(TextUtils.isEmpty(requirementInfo.getPrefer_sex()) ? "" : arr_desisex[Integer.parseInt(requirementInfo.getPrefer_sex())]);
                act_edit_req_work_type_content.setText(TextUtils.isEmpty(requirementInfo.getWork_type()) ? "" : arr_worktype[Integer.parseInt(requirementInfo.getWork_type())]);               */
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
//                    act_edit_req_city_content.setText(requirementInfo.getProvince() + requirementInfo.getCity() + itemMap.value);
//                    requirementInfo.setDistrict(itemMap.value);
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
                case Constant.REQUIRECODE_PERSONS:
                    act_edit_req_persons_content.setText(itemMap.value);
                    requirementInfo.setFamily_description(itemMap.value);
                    break;
                case Constant.REQUIRECODE_HOUSETYPE:
                    act_edit_req_housetype_content.setText(itemMap.value);
                    requirementInfo.setHouse_type(itemMap.key);
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
}

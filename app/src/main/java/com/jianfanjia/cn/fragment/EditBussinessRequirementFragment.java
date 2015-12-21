package com.jianfanjia.cn.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jianfanjia.cn.activity.requirement.EditRequirementItemActivity_;
import com.jianfanjia.cn.activity.requirement.EditRequirementLovestyleActivity_;
import com.jianfanjia.cn.activity.R;
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
@EFragment(R.layout.fragment_edit_bussiness_req)
public class EditBussinessRequirementFragment extends BaseAnnotationFragment {

    private NotifyActivityStatusChange hostActivity;
    protected boolean isFinish = false;//编辑是否已经完成
    private int actionType = -1;//创建需求or修改需求

    @ViewById
    protected TextView act_edit_req_city_content;//所在城市
    @ViewById
    protected EditText act_edit_req_cell_content;//小区
    @ViewById
    protected TextView act_edit_req_lovestyle_content;//风格喜好
    @ViewById
    protected TextView act_edit_req_lovedesistyle_content;//偏好设计师类型
    @ViewById
    protected TextView act_edit_req_decoratetype_content;//装修类型
    @ViewById
    protected TextView act_edit_req_work_type_content;//包工类型
    @ViewById
    protected TextView act_edit_req_lovedesisex_content;//偏好设计师性别
    @ViewById
    protected EditText act_edit_req_street_content;//所在街道
    @ViewById
    protected EditText act_edit_req_housearea_content;//装修面积
    @ViewById
    protected EditText act_edit_req_decoratebudget_content;//装修预算

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
    @StringArrayRes(R.array.arr_busi_housetype)
    protected String[] arr_busihousetype;

    private Intent gotoItem;
    private Intent gotoItemLove;
    private RequirementInfo requirementInfo;

    @AfterTextChange({R.id.act_edit_req_cell_content, R.id.act_edit_req_street_content, R.id.act_edit_req_housearea_content, R.id.act_edit_req_decoratebudget_content})
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
        if (act_edit_req_city_content.length() > 0 && !act_edit_req_city_content.getText().equals(getResources().getString(R.string.city_tip))
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
        LogTool.d(this.getClass().getName(),"isFinish = " + isFinish);

        hostActivity.notifyStatusChange();
    }

    public boolean isFinish() {
        return isFinish;
    }

    @Click({R.id.act_edit_req_city, R.id.act_edit_req_decoratetype,
            R.id.act_edit_req_lovestyle,
            R.id.act_edit_req_persons,
            R.id.act_edit_req_lovedesistyle,
            R.id.act_edit_req_lovedesisex,
            R.id.act_edit_req_work_type})
    protected void back(View clickView) {
        int viewId = clickView.getId();
        switch (viewId) {
            case R.id.act_edit_req_city:
                gotoItem.putExtra(Global.REQUIRE_DATA, Constant.REQUIRECODE_CITY);
                startActivityForResult(gotoItem, Constant.REQUIRECODE_CITY);
                break;
            case R.id.act_edit_req_lovedesistyle:
                gotoItem.putExtra(Global.REQUIRE_DATA, Constant.REQUIRECODE_LOVEDESISTYLE);
                startActivityForResult(gotoItem, Constant.REQUIRECODE_LOVEDESISTYLE);
                break;
            case R.id.act_edit_req_lovestyle:
                gotoItemLove.putExtra(Global.REQUIRE_DATA, Constant.REQUIRECODE_LOVESTYLE);
                startActivityForResult(gotoItemLove, Constant.REQUIRECODE_LOVESTYLE);
                break;
            case R.id.act_edit_req_decoratetype:
                gotoItem.putExtra(Global.REQUIRE_DATA, Constant.REQUIRECODE_BUSI_DECORATETYPE);
                startActivityForResult(gotoItem, Constant.REQUIRECODE_BUSI_DECORATETYPE);
                break;
            case R.id.act_edit_req_work_type:
                gotoItem.putExtra(Global.REQUIRE_DATA, Constant.REQUIRECODE_WORKTYPE);
                startActivityForResult(gotoItem, Constant.REQUIRECODE_WORKTYPE);
                break;
            case R.id.act_edit_req_lovedesisex:
                gotoItem.putExtra(Global.REQUIRE_DATA, Constant.REQUIRECODE_DESISEX);
                startActivityForResult(gotoItem, Constant.REQUIRECODE_DESISEX);
                break;
            default:
                break;
        }
    }

    @AfterViews
    protected void setMainHeadView() {

        gotoItem = new Intent(getActivity(), EditRequirementItemActivity_.class);
        gotoItemLove = new Intent(getActivity(), EditRequirementLovestyleActivity_.class);

        initData();
    }

    private void initData() {
        LogTool.d(this.getClass().getName(), "initData");
        requirementInfo = (RequirementInfo) getArguments().getSerializable(Global.REQUIREMENT_INFO);
        actionType = getArguments().getInt(Global.REQUIREMENG_ACTION_TYPE, 0);
        switch (actionType) {
            case XuQiuFragment.REQUESTCODE_EDIT_REQUIREMENT:
                act_edit_req_city_content.setText(requirementInfo.getProvince() + requirementInfo.getCity() + requirementInfo.getDistrict());
                act_edit_req_street_content.setText(requirementInfo.getStreet());
                act_edit_req_cell_content.setText(requirementInfo.getCell());
                act_edit_req_housearea_content.setText(requirementInfo.getHouse_area());
                act_edit_req_decoratebudget_content.setText(requirementInfo.getTotal_price());
                act_edit_req_decoratetype_content.setText(TextUtils.isEmpty(requirementInfo.getBusiness_house_type()) ? "" : arr_busihousetype[Integer.parseInt(requirementInfo.getBusiness_house_type()) > (arr_busihousetype.length - 1) ? (arr_busihousetype.length - 1) : Integer.parseInt(requirementInfo.getBusiness_house_type())]);
                act_edit_req_lovestyle_content.setText(TextUtils.isEmpty(requirementInfo.getDec_style()) ? "" : arr_lovestyle[Integer.parseInt(requirementInfo.getDec_style())]);
                act_edit_req_lovedesistyle_content.setText(TextUtils.isEmpty(requirementInfo.getCommunication_type()) ? "" : arr_love_designerstyle[Integer.parseInt(requirementInfo.getCommunication_type())]);
                act_edit_req_lovedesisex_content.setText(TextUtils.isEmpty(requirementInfo.getPrefer_sex()) ? "" : arr_desisex[Integer.parseInt(requirementInfo.getPrefer_sex())]);
                act_edit_req_work_type_content.setText(TextUtils.isEmpty(requirementInfo.getWork_type()) ? "" : arr_worktype[Integer.parseInt(requirementInfo.getWork_type())]);
                break;
            case XuQiuFragment.REQUESTCODE_PUBLISH_REQUIREMENT:
                act_edit_req_city_content.setText(getString(R.string.city_tip));
                act_edit_req_lovestyle_content.setText(TextUtils.isEmpty(requirementInfo.getDec_style()) ? "" : arr_lovestyle[Integer.parseInt(requirementInfo.getDec_style())]);
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
                    act_edit_req_city_content.setText(requirementInfo.getProvince() + requirementInfo.getCity() + itemMap.value);
                    requirementInfo.setDistrict(itemMap.value);
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
                    act_edit_req_work_type_content.setText(itemMap.value);
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

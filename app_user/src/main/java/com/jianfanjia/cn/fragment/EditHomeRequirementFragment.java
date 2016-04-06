package com.jianfanjia.cn.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import com.jianfanjia.api.model.Requirement;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.my.EditCityActivity;
import com.jianfanjia.cn.activity.requirement.EditRequirementItemActivity;
import com.jianfanjia.cn.activity.requirement.EditRequirementLovestyleActivity;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.business.RequirementBusiness;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.constant.IntentConstant;
import com.jianfanjia.cn.interf.NotifyActivityStatusChange;
import com.jianfanjia.cn.interf.cutom_annotation.ReqItemFinderImp;
import com.jianfanjia.common.tool.LogTool;

/**
 * Description: com.jianfanjia.cn.fragment
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-12-15 14:43
 */
public class EditHomeRequirementFragment extends BaseFragment {

    private NotifyActivityStatusChange hostActivity;
    private boolean isFinish = false;//是否所有字段都填了
    private int actionType = -1;//创建需求or修改需求

    @Bind(R.id.scrollView)
    protected ScrollView rootScrollView;

    @Bind(R.id.act_edit_req_city_content)
    protected TextView act_edit_req_city_content;//所在城市

    @Bind(R.id.act_edit_req_cell_content)
    protected EditText act_edit_req_cell_content;//小区

    @Bind(R.id.act_edit_req_dong_content)
    protected EditText act_edit_req_dong_content;//门牌号

    @Bind(R.id.act_edit_req_housetype_content)
    protected TextView act_edit_req_housetype_content;//户型

    @Bind(R.id.act_edit_req_persons_content)
    protected TextView act_edit_req_persons_content;//计划常住人口

    @Bind(R.id.act_edit_req_lovestyle_content)
    protected TextView act_edit_req_lovestyle_content;//风格喜好

    @Bind(R.id.act_edit_req_lovedesistyle_content)
    protected TextView act_edit_req_lovedesistyle_content;//偏好设计师类型

    @Bind(R.id.act_edit_req_work_type_content)
    protected TextView act_edit_req_work_type_content;//包工类型

    @Bind(R.id.act_edit_req_lovedesisex_content)
    protected TextView act_edit_req_lovedesisex_content;//偏好设计师性别

    @Bind(R.id.act_edit_req_housearea_content)
    protected EditText act_edit_req_housearea_content;//装修面积

    @Bind(R.id.act_edit_req_decoratebudget_content)
    protected EditText act_edit_req_decoratebudget_content;//装修预算


    @Bind(R.id.act_edit_req_decoratebudget_365)
    protected LinearLayout budget365Layout;

    @Bind(R.id.decoratebudget_365_basic_price)
    protected TextView budget365BasicPriceView;

    @Bind(R.id.decoratebudget_365_individuation_price_layout)
    protected LinearLayout budget365IndividuatiuonPriceLayout;

    @Bind(R.id.decoratebudget_365_individuation_error)
    protected ImageView budget365IndividuatiuonErrorView;

    @Bind(R.id.decoratebudget_365_individuation_price)
    protected TextView budget365IndividuationPriceView;

    @Bind(R.id.decoratebudget_365_total_price_layout)
    protected LinearLayout budget365TotalPriceLayout;

    @Bind(R.id.decoratebudget_365_total_error)
    protected ImageView budget365TotalErrorView;

    @Bind(R.id.decoratebudget_365_total_price)
    protected TextView budget365TotalPriceView;

    @Bind(R.id.act_edit_req_decoratebudget_365_alert)
    protected TextView budget365AlertView;

    protected String[] arr_lovestyle;
    protected String[] arr_housetype;
    protected String[] arr_love_designerstyle;
    protected String[] arr_worktype;
    protected String[] arr_desisex;


    private Requirement requirementInfo;
    private boolean isShowBudget365;//是否显示365包
    private boolean isTotalBudegetCorrect = false;//总预算是否正确

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        initView();
        initData();
        return view;
    }

    public void initView() {
        initStringArray();

        isShowBudget365 = false;
        budget365Layout.setVisibility(View.GONE);
        budget365IndividuatiuonPriceLayout.setVisibility(View.GONE);
        budget365TotalPriceLayout.setVisibility(View.GONE);
    }

    private void initStringArray() {
        arr_lovestyle = getResources().getStringArray(R.array.arr_decstyle);
        arr_housetype = getResources().getStringArray(R.array.arr_housetype);
        arr_love_designerstyle = getResources().getStringArray(R.array.arr_love_designerstyle);
        arr_worktype = getResources().getStringArray(R.array.arr_worktype);
        arr_desisex = getResources().getStringArray(R.array.arr_desisex);
    }

    @OnTextChanged(value = R.id.act_edit_req_cell_content, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    protected void cellAfterChanged(CharSequence charSequence) {
        requirementInfo.setBasic_address(charSequence.toString());
        isAllInput();
    }

    @OnTextChanged(value = R.id.act_edit_req_dong_content, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    protected void dongAfterChanged(CharSequence charSequence) {
        requirementInfo.setDetail_address(charSequence.toString());
        isAllInput();
    }

    @OnTextChanged(value = R.id.act_edit_req_housearea_content, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    protected void houseareaAfterChanged(CharSequence charSequence) {
        requirementInfo.setHouse_area(charSequence.toString());
        showOrHide365Layout(charSequence);
        isAllInput();
    }

    private void showOrHide365Layout(CharSequence charSequence) {
        if (!TextUtils.isEmpty(charSequence.toString())) {
            int area = Integer.parseInt(charSequence.toString());
            if (RequirementBusiness.isAreaBelong365(area)) {
                budget365Layout.setVisibility(View.VISIBLE);
                isShowBudget365 = true;

                //设置基础价格
                float basicPrice = (float) area * RequirementBusiness.PRICE_EVERY_UNIT_365 / RequirementBusiness
                        .TEN_THOUSAND;
                budget365BasicPriceView.setText(RequirementBusiness.covertPriceToShow(basicPrice));

                settingTotalBudget(act_edit_req_decoratebudget_content.getEditableText().toString(), basicPrice);
            } else {
                budget365Layout.setVisibility(View.GONE);
                isShowBudget365 = false;
            }
        } else {
            budget365Layout.setVisibility(View.GONE);
            isShowBudget365 = false;
        }
    }

    private void settingTotalBudget(CharSequence charSequence, float basicPrice) {
        if (!TextUtils.isEmpty(charSequence.toString())) {

            float total = Float.parseFloat(charSequence.toString());
            String totalText = RequirementBusiness.covertPriceToShow(total);
            String individuationText = RequirementBusiness.covertPriceToShow(total - basicPrice);

            LogTool.d(this.getClass().getName(), "tatoal =" + totalText + "  basicPrice =" + basicPrice);
            if (total >= basicPrice) {
                budget365AlertView.setVisibility(View.GONE);
                isTotalBudegetCorrect = true;

                budget365IndividuatiuonPriceLayout.setVisibility(View.VISIBLE);
                budget365TotalPriceLayout.setVisibility(View.VISIBLE);
                budget365TotalPriceView.setText(totalText);
                budget365IndividuationPriceView.setText(individuationText);

                budget365IndividuatiuonErrorView.setVisibility(View.GONE);
                budget365TotalErrorView.setVisibility(View.GONE);
            } else {
                budget365AlertView.setVisibility(View.VISIBLE);
                isTotalBudegetCorrect = false;

                budget365IndividuatiuonPriceLayout.setVisibility(View.GONE);
                budget365TotalPriceLayout.setVisibility(View.GONE);
                budget365IndividuatiuonErrorView.setVisibility(View.VISIBLE);
                budget365TotalErrorView.setVisibility(View.VISIBLE);
            }
        } else {

            isTotalBudegetCorrect = false;
            budget365IndividuatiuonPriceLayout.setVisibility(View.GONE);
            budget365TotalPriceLayout.setVisibility(View.GONE);
            budget365IndividuatiuonErrorView.setVisibility(View.VISIBLE);
            budget365TotalErrorView.setVisibility(View.VISIBLE);

        }

    }

    @OnTextChanged(value = R.id.act_edit_req_decoratebudget_content, callback = OnTextChanged.Callback
            .AFTER_TEXT_CHANGED)
    protected void decoratebudgetAfterChanged(CharSequence charSequence) {
        requirementInfo.setTotal_price(charSequence.toString());
        showOrHide365Layout(act_edit_req_housearea_content.getEditableText().toString());
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
                && act_edit_req_dong_content.length() > 0
                && act_edit_req_housetype_content.length() > 0
                && act_edit_req_lovedesisex_content.length() > 0
                && act_edit_req_lovestyle_content.length() > 0
                && act_edit_req_persons_content.length() > 0
                && act_edit_req_dong_content.length() > 0
                && act_edit_req_work_type_content.length() > 0) {
            if (isShowBudget365) {//如果符合365基础包，就按照365基础包方式判断
                if (!isTotalBudegetCorrect) {
                    isFinish = false;
                } else {
                    isFinish = true;
                    requirementInfo.setPackage_type(RequirementBusiness.PACKGET_365);
                }
            } else {//不符合365基础包，简单判断一下
                if (act_edit_req_decoratebudget_content.length() > 0
                        && act_edit_req_housearea_content.length() > 0) {
                    isFinish = true;
                    requirementInfo.setPackage_type(RequirementBusiness.PACKGET_DEFAULT);
                } else {
                    isFinish = false;
                }
            }
        } else {
            isFinish = false;
        }
        LogTool.d(this.getClass().getName(), "isFinish = " + isFinish);
        hostActivity.notifyStatusChange();
    }

    @OnClick({R.id.act_edit_req_city, R.id.act_edit_req_housetype,
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
                loveDesignerBundle.putInt(IntentConstant.REQUIRE_DATA, Constant.REQUIRECODE_LOVEDESISTYLE);
                startActivityForResult(EditRequirementItemActivity.class, loveDesignerBundle, Constant
                        .REQUIRECODE_LOVEDESISTYLE);
                break;
            case R.id.act_edit_req_lovestyle:
                Bundle loveStyleBundle = new Bundle();
                loveStyleBundle.putInt(IntentConstant.REQUIRE_DATA, Constant.REQUIRECODE_LOVESTYLE);
                startActivityForResult(EditRequirementLovestyleActivity.class, loveStyleBundle, Constant
                        .REQUIRECODE_LOVESTYLE);
                break;
            case R.id.act_edit_req_persons:
                Bundle personBundle = new Bundle();
                personBundle.putInt(IntentConstant.REQUIRE_DATA, Constant.REQUIRECODE_PERSONS);
                startActivityForResult(EditRequirementItemActivity.class, personBundle, Constant.REQUIRECODE_PERSONS);
                break;
            case R.id.act_edit_req_housetype:
                Bundle houseTypeBundle = new Bundle();
                houseTypeBundle.putInt(IntentConstant.REQUIRE_DATA, Constant.REQUIRECODE_HOUSETYPE);
                startActivityForResult(EditRequirementItemActivity.class, houseTypeBundle, Constant
                        .REQUIRECODE_HOUSETYPE);
                break;
            case R.id.act_edit_req_work_type:
                Bundle workTypeBundle = new Bundle();
                workTypeBundle.putInt(IntentConstant.REQUIRE_DATA, Constant.REQUIRECODE_WORKTYPE);
                startActivityForResult(EditRequirementItemActivity.class, workTypeBundle, Constant
                        .REQUIRECODE_WORKTYPE);
                break;
            case R.id.act_edit_req_lovedesisex:
                Bundle lovedesisexBundle = new Bundle();
                lovedesisexBundle.putInt(IntentConstant.REQUIRE_DATA, Constant.REQUIRECODE_DESISEX);
                startActivityForResult(EditRequirementItemActivity.class, lovedesisexBundle, Constant
                        .REQUIRECODE_DESISEX);
                break;
            default:
                break;
        }
    }

    private void initData() {
        LogTool.d(this.getClass().getName(), "initData");
        requirementInfo = (Requirement) getArguments().getSerializable(IntentConstant.REQUIREMENT_INFO);
        actionType = getArguments().getInt(IntentConstant.REQUIREMENG_ACTION_TYPE, 0);
        switch (actionType) {
            case XuQiuFragment.REQUESTCODE_EDIT_REQUIREMENT:
            case XuQiuFragment.REQUESTCODE_PUBLISH_REQUIREMENT:
                if (!TextUtils.isEmpty(requirementInfo.getProvince()) && !TextUtils.isEmpty(requirementInfo.getCity()
                ) && !TextUtils.isEmpty(requirementInfo.getDistrict())) {
                    act_edit_req_city_content.setText(requirementInfo.getProvince() + requirementInfo.getCity() +
                            requirementInfo.getDistrict());
                }
                if (!TextUtils.isEmpty(requirementInfo.getBasic_address())) {
                    act_edit_req_cell_content.setText(requirementInfo.getBasic_address());
                }
                if (!TextUtils.isEmpty(requirementInfo.getDetail_address())) {
                    act_edit_req_dong_content.setText(requirementInfo.getDetail_address());
                }
                if (!TextUtils.isEmpty(requirementInfo.getHouse_area())) {
                    act_edit_req_housearea_content.setText(requirementInfo.getHouse_area());
                }
                if (!TextUtils.isEmpty(requirementInfo.getTotal_price())) {
                    act_edit_req_decoratebudget_content.setText(requirementInfo.getTotal_price());
                }
                act_edit_req_housetype_content.setText(TextUtils.isEmpty(requirementInfo.getHouse_type()) ? "" :
                        arr_housetype[Integer.parseInt(requirementInfo.getHouse_type())]);
                act_edit_req_persons_content.setText(TextUtils.isEmpty(requirementInfo.getFamily_description()) ? ""
                        : requirementInfo.getFamily_description());
                act_edit_req_lovestyle_content.setText(TextUtils.isEmpty(requirementInfo.getDec_style()) ? "" :
                        arr_lovestyle[Integer.parseInt(requirementInfo.getDec_style())]);
                act_edit_req_lovedesistyle_content.setText(TextUtils.isEmpty(requirementInfo.getCommunication_type())
                        ? "" : arr_love_designerstyle[Integer.parseInt(requirementInfo.getCommunication_type())]);
                act_edit_req_lovedesisex_content.setText(TextUtils.isEmpty(requirementInfo.getPrefer_sex()) ? "" :
                        arr_desisex[Integer.parseInt(requirementInfo.getPrefer_sex())]);
                act_edit_req_work_type_content.setText(TextUtils.isEmpty(requirementInfo.getWork_type()) ? "" :
                        arr_worktype[Integer.parseInt(requirementInfo.getWork_type())]);
                break;
        }
    }

    public Requirement getRequirementInfo() {
        return requirementInfo;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (data != null) {
            ReqItemFinderImp.ItemMap itemMap = (ReqItemFinderImp.ItemMap) data.getSerializableExtra(IntentConstant
                    .RESPONSE_DATA);
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

    @Override
    public int getLayoutId() {
        return R.layout.fragment_edt_home_req;
    }
}

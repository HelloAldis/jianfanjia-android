package com.jianfanjia.cn.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import com.jianfanjia.api.model.Requirement;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.constant.IntentConstant;
import com.jianfanjia.cn.ui.activity.common.choose_item.ChooseItemIntent;
import com.jianfanjia.cn.ui.activity.common.choose_item.ChooseItemLoveStyleIntent;
import com.jianfanjia.cn.ui.activity.my.EditCityActivity;
import com.jianfanjia.cn.ui.interf.NotifyActivityStatusChange;
import com.jianfanjia.cn.ui.interf.cutom_annotation.ReqItemFinderImp;
import com.jianfanjia.common.tool.LogTool;

/**
 * Description: com.jianfanjia.cn.fragment
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-12-15 14:43
 */
public class EditBussinessRequirementFragment extends BaseFragment {
    private static final String TAG = EditBussinessRequirementFragment.class.getName();
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
    protected EditText act_edit_req_street_content;//所在街道
    @Bind(R.id.act_edit_req_housearea_content)
    protected EditText act_edit_req_housearea_content;//装修面积
    @Bind(R.id.act_edit_req_decoratebudget_content)
    protected EditText act_edit_req_decoratebudget_content;//装修预算
    @Bind(R.id.act_edit_req_cell_content)
    protected EditText act_edit_req_cell_content;//小区

    protected String[] arr_lovestyle;
    protected String[] arr_housetype;
    protected String[] arr_love_designerstyle;
    protected String[] arr_worktype;
    protected String[] arr_busihousetype;
    protected String[] arr_desisex;

    private Requirement requirementInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        initView();
        initData();
        return view;
    }

    public void initView() {
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

    @OnTextChanged(value = R.id.act_edit_req_cell_content, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    protected void cellAfterChanged(CharSequence charSequence) {
        requirementInfo.setBasic_address(charSequence.toString());
        isAllInput();
    }

    @OnTextChanged(value = R.id.act_edit_req_street_content, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    protected void streetAfterChanged(CharSequence charSequence) {
        requirementInfo.setDetail_address(charSequence.toString());
        isAllInput();
    }

    @OnTextChanged(value = R.id.act_edit_req_housearea_content, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    protected void houseareaAfterChanged(CharSequence charSequence) {
        if (!TextUtils.isEmpty(charSequence.toString())) {
            requirementInfo.setHouse_area(Integer.parseInt(charSequence.toString()));
        } else {
            requirementInfo.setHouse_area(0);
        }
        isAllInput();
    }

    @OnTextChanged(value = R.id.act_edit_req_decoratebudget_content, callback = OnTextChanged.Callback
            .AFTER_TEXT_CHANGED)
    protected void decoratebudgetAfterChanged(CharSequence charSequence) {
        if (!TextUtils.isEmpty(charSequence.toString())) {
            requirementInfo.setTotal_price(Integer.parseInt(charSequence.toString()));
        } else {
            requirementInfo.setTotal_price(0);
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
        LogTool.d(TAG, "isFinish = " + isFinish);
        hostActivity.notifyStatusChange();
    }

    public boolean isFinish() {
        return isFinish;
    }

    @OnClick({R.id.act_edit_req_city, R.id.act_edit_req_decoratetype,
            R.id.act_edit_req_lovestyle,
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
                ChooseItemIntent loveDesignerStyleIntent = new ChooseItemIntent(getContext());
                loveDesignerStyleIntent.setSingleChoose(Constant.REQUIRECODE_LOVEDESISTYLE, requirementInfo
                        .getCommunication_type(), getString(R.string.str_lovedesistyle));
                startActivityForResult(loveDesignerStyleIntent, Constant.REQUIRECODE_LOVEDESISTYLE);
                break;
            case R.id.act_edit_req_lovestyle:
                ChooseItemLoveStyleIntent loveStyleIntent = new ChooseItemLoveStyleIntent(getContext());
                loveStyleIntent.setSingleChoose(Constant.REQUIRECODE_LOVESTYLE, requirementInfo
                        .getDec_style(), getString(R.string.str_lovestyle));
                startActivityForResult(loveStyleIntent, Constant.REQUIRECODE_LOVESTYLE);
                break;
            case R.id.act_edit_req_decoratetype:
                ChooseItemIntent houseTypeIntent = new ChooseItemIntent(getContext());
                houseTypeIntent.setSingleChoose(Constant.REQUIRECODE_BUSI_DECORATETYPE, requirementInfo
                                .getBusiness_house_type(),
                        getString(R.string.str_businessdecoratetype));
                startActivityForResult(houseTypeIntent, Constant.REQUIRECODE_BUSI_DECORATETYPE);
                break;
            case R.id.act_edit_req_work_type:
                ChooseItemIntent workTypeIntent = new ChooseItemIntent(getContext());
                workTypeIntent.setSingleChoose(Constant.REQUIRECODE_WORKTYPE, requirementInfo.getWork_type(),
                        getString(R.string.str_work_type));
                startActivityForResult(workTypeIntent, Constant.REQUIRECODE_WORKTYPE);
                break;
            case R.id.act_edit_req_lovedesisex:
                ChooseItemIntent loveDesignerSexIntent = new ChooseItemIntent(getContext());
                loveDesignerSexIntent.setSingleChoose(Constant.REQUIRECODE_DESISEX, requirementInfo.getPrefer_sex(),
                        getString(R.string.str_lovedesisex));
                startActivityForResult(loveDesignerSexIntent, Constant.REQUIRECODE_DESISEX);
                break;
            default:
                break;
        }
    }

    private void initData() {
        LogTool.d(TAG, "initData");
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
                if (!TextUtils.isEmpty(requirementInfo.getDetail_address())) {
                    act_edit_req_street_content.setText(requirementInfo.getDetail_address());
                }
                if (!TextUtils.isEmpty(requirementInfo.getBasic_address())) {
                    act_edit_req_cell_content.setText(requirementInfo.getBasic_address());
                }
                if (requirementInfo.getHouse_area() != 0) {
                    act_edit_req_housearea_content.setText(requirementInfo.getHouse_area() + "");
                }
                if (requirementInfo.getTotal_price() != 0) {
                    act_edit_req_decoratebudget_content.setText(requirementInfo.getTotal_price() + "");
                }
                act_edit_req_decoratetype_content.setText(TextUtils.isEmpty(requirementInfo.getBusiness_house_type())
                        ? "" : arr_busihousetype[Integer.parseInt(requirementInfo.getBusiness_house_type()) >
                        (arr_busihousetype.length - 1) ? (arr_busihousetype.length - 1) : Integer.parseInt
                        (requirementInfo.getBusiness_house_type())]);
                act_edit_req_lovestyle_content.setText(TextUtils.isEmpty(requirementInfo.getDec_style()) ? "" :
                        arr_lovestyle[Integer.parseInt(requirementInfo.getDec_style())]);
                act_edit_req_lovedesistyle_content.setText(TextUtils.isEmpty(requirementInfo.getCommunication_type())
                        ? getResources().getString(R.string.no_limit) : arr_love_designerstyle[Integer.parseInt
                        (requirementInfo.getCommunication_type())]);
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
            ReqItemFinderImp.ItemMap itemMap = data.getParcelableExtra(IntentConstant
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

package com.jianfanjia.cn.designer.ui.activity.requirement;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.api.model.Requirement;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.designer.business.RequirementBusiness;
import com.jianfanjia.cn.designer.config.Global;
import com.jianfanjia.cn.designer.tools.UiHelper;
import com.jianfanjia.cn.designer.view.MainHeadView;

/**
 * Description: com.jianfanjia.cn.activity
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-10-15 13:19
 */
public class PreviewHomeRequirementActivity extends BaseSwipeBackActivity {

    @Bind(R.id.act_edit_req_head)
    protected MainHeadView mainHeadView;
    @Bind(R.id.act_edit_req_city_content)
    protected TextView act_edit_req_city_content;//所在城市
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
    protected TextView act_edit_req_housearea_content;//装修面积
    @Bind(R.id.act_edit_req_decoratebudget_content)
    protected TextView act_edit_req_decoratebudget_content;//装修预算
    @Bind(R.id.act_edit_req_cell_content)
    protected TextView act_edit_req_cell_content;//小区
    @Bind(R.id.act_edit_req_dong_content)
    protected TextView act_edit_req_dong_content;//期

    protected String[] arr_lovestyle;
    protected String[] arr_housetype;
    protected String[] arr_love_designerstyle;
    protected String[] arr_worktype;
    protected String[] arr_desisex;

    @Bind(R.id.act_edit_req_decoratebudget_365)
    protected LinearLayout budget365Layout;

    @Bind(R.id.act_edit_req_decoratebudget_high_point)
    protected LinearLayout budgetHighPointLayout;

    @Bind(R.id.decoratebudget_365_basic_price)
    protected TextView budget365BasicPriceView;

    @Bind(R.id.decoratebudget_365_individuation_price)
    protected TextView budget365IndividuationPriceView;

    @Bind(R.id.decoratebudget_365_total_price)
    protected TextView budget365TotalPriceView;

    private Requirement requirementInfo;

    @OnClick({R.id.head_back_layout, R.id.act_edit_req_decoratebudget_365_detail})
    protected void back(View clickView) {
        int viewId = clickView.getId();
        switch (viewId) {
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
            case R.id.act_edit_req_decoratebudget_365_detail:
                UiHelper.intentToPackget365Detail(this);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    public void initView() {
        mainHeadView.setMianTitle(getResources().getString(R.string.str_priview_home_req));
        mainHeadView.setRightTitleVisable(View.GONE);

        initStringArray();
    }

    private void initStringArray() {
        arr_lovestyle = getResources().getStringArray(R.array.arr_decstyle);
        arr_housetype = getResources().getStringArray(R.array.arr_housetype);
        arr_love_designerstyle = getResources().getStringArray(R.array.arr_love_designerstyle);
        arr_worktype = getResources().getStringArray(R.array.arr_worktype);
        arr_desisex = getResources().getStringArray(R.array.arr_desisex);
    }

    private void initData() {
        Intent intent = getIntent();
        requirementInfo = (Requirement) intent.getSerializableExtra(Global.REQUIREMENT_INFO);
        if (requirementInfo != null) {
            act_edit_req_city_content.setText(requirementInfo.getProvince() + requirementInfo.getCity() +
                    requirementInfo.getDistrict());
            act_edit_req_cell_content.setText(requirementInfo.getBasic_address());
            act_edit_req_dong_content.setText(requirementInfo.getDetail_address());
            act_edit_req_housearea_content.setText(requirementInfo.getHouse_area() + "");
            act_edit_req_housetype_content.setText(TextUtils.isEmpty(requirementInfo.getHouse_type()) ? "" :
                    arr_housetype[Integer.parseInt(requirementInfo.getHouse_type())]);
            act_edit_req_decoratebudget_content.setText(requirementInfo.getTotal_price() + "");
            act_edit_req_persons_content.setText(requirementInfo.getFamily_description());
            act_edit_req_lovestyle_content.setText(TextUtils.isEmpty(requirementInfo.getDec_style()) ? "" :
                    arr_lovestyle[Integer.parseInt(requirementInfo.getDec_style())]);
            act_edit_req_lovedesistyle_content.setText(TextUtils.isEmpty(requirementInfo.getCommunication_type()) ?
                    "" : arr_love_designerstyle[Integer.parseInt(requirementInfo.getCommunication_type())]);
            act_edit_req_lovedesisex_content.setText(TextUtils.isEmpty(requirementInfo.getPrefer_sex()) ? "" :
                    arr_desisex[Integer.parseInt(requirementInfo.getPrefer_sex())]);
            act_edit_req_work_type_content.setText(TextUtils.isEmpty(requirementInfo.getWork_type()) ? "" :
                    arr_worktype[Integer.parseInt(requirementInfo.getWork_type())]);

            initBudget365Layout();
        }
    }

    private void initBudget365Layout() {
        String pageType = requirementInfo.getPackage_type();

        switch (pageType){
            case RequirementBusiness.PACKGET_DEFAULT:
                budget365Layout.setVisibility(View.GONE);
                budgetHighPointLayout.setVisibility(View.GONE);
                break;
            case RequirementBusiness.PACKGET_365:
                budget365Layout.setVisibility(View.VISIBLE);
                budgetHighPointLayout.setVisibility(View.GONE);
                update365Layout();
                break;
            case RequirementBusiness.PACKGET_HIGH_POINT:
                budget365Layout.setVisibility(View.GONE);
                budgetHighPointLayout.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void update365Layout(){

        int houseArea = requirementInfo.getHouse_area();
        int totalPrice = requirementInfo.getTotal_price();
        float basicPrice = (float) houseArea * RequirementBusiness.PRICE_EVERY_UNIT_365 / RequirementBusiness
                .TEN_THOUSAND;

        float individuationPrice = totalPrice - basicPrice;

        budget365BasicPriceView.setText(RequirementBusiness.covertPriceToShow(basicPrice));
        budget365TotalPriceView.setText(RequirementBusiness.covertPriceToShow(totalPrice));
        budget365IndividuationPriceView.setText(RequirementBusiness.covertPriceToShow(individuationPrice));
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_priview_req;
    }
}

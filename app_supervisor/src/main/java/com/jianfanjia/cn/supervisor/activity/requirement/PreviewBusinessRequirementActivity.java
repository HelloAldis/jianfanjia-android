package com.jianfanjia.cn.supervisor.activity.requirement;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.api.model.Requirement;
import com.jianfanjia.cn.supervisor.R;
import com.jianfanjia.cn.supervisor.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.supervisor.config.Global;
import com.jianfanjia.cn.view.MainHeadView;


/**
 * Description: com.jianfanjia.cn.activity
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-10-15 13:19
 */
public class PreviewBusinessRequirementActivity extends BaseSwipeBackActivity {

    @Bind(R.id.act_edit_req_head)
    protected MainHeadView mainHeadView;
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

    private Requirement requirementInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    public void initView() {
        mainHeadView.setMianTitle(getResources().getString(R.string.str_priview_business_req));
        mainHeadView.setRightTitleVisable(View.GONE);
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

    @OnClick({R.id.head_back_layout})
    protected void back(View clickView) {
        int viewId = clickView.getId();
        switch (viewId) {
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
            default:
                break;
        }
    }

    private void initData() {
        Intent intent = getIntent();
        requirementInfo = (Requirement) intent.getSerializableExtra(Global.REQUIREMENT_INFO);
        if (requirementInfo != null) {
            act_edit_req_city_content.setText(requirementInfo.getProvince() + requirementInfo.getCity() +
                    requirementInfo.getDistrict());
            act_edit_req_street_content.setText(TextUtils.isEmpty(requirementInfo.getBasic_address()) ? "" :
                    requirementInfo
                            .getBasic_address());
            act_edit_req_cell_content.setText(requirementInfo.getDetail_address());
            act_edit_req_housearea_content.setText(requirementInfo.getHouse_area() + "");
            act_edit_req_decoratebudget_content.setText(requirementInfo.getTotal_price() + "");
            act_edit_req_decoratetype_content.setText(TextUtils.isEmpty(requirementInfo.getBusiness_house_type()) ?
                    "" : arr_busihousetype[Integer.parseInt(requirementInfo.getBusiness_house_type()) >
                    (arr_busihousetype.length - 1) ? (arr_busihousetype.length - 1) : Integer.parseInt
                    (requirementInfo.getBusiness_house_type())]);
            act_edit_req_lovestyle_content.setText(TextUtils.isEmpty(requirementInfo.getDec_style()) ? "" :
                    arr_lovestyle[Integer.parseInt(requirementInfo.getDec_style())]);
            act_edit_req_lovedesistyle_content.setText(TextUtils.isEmpty(requirementInfo.getCommunication_type()) ?
                    "" : arr_love_designerstyle[Integer.parseInt(requirementInfo.getCommunication_type())]);
            act_edit_req_lovedesisex_content.setText(TextUtils.isEmpty(requirementInfo.getPrefer_sex()) ? "" :
                    arr_desisex[Integer.parseInt(requirementInfo.getPrefer_sex())]);
            act_edit_req_work_type_content.setText(TextUtils.isEmpty(requirementInfo.getWork_type()) ? "" :
                    arr_worktype[Integer.parseInt(requirementInfo.getWork_type())]);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_priview_busi_req;
    }
}

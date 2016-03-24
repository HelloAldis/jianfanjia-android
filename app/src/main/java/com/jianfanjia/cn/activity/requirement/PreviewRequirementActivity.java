package com.jianfanjia.cn.activity.requirement;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.SwipeBackActivity;
import com.jianfanjia.cn.bean.RequirementInfo;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.view.MainHeadView;

/**
 * Description: com.jianfanjia.cn.activity
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-10-15 13:19
 */
public class PreviewRequirementActivity extends SwipeBackActivity {

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
    @Bind(R.id.act_edit_req_qi_content)
    protected TextView act_edit_req_qi_content;//期
    @Bind(R.id.act_edit_req_danyuan_content)
    protected TextView act_edit_req_danyuan_content;//单元
    @Bind(R.id.act_edit_req_dong_content)
    protected TextView act_edit_req_dong_content;//栋
    @Bind(R.id.act_edit_req_shi_content)
    protected TextView act_edit_req_shi_content;//室

    protected String[] arr_lovestyle;
    protected String[] arr_housetype;
    protected String[] arr_love_designerstyle;
    protected String[] arr_worktype;
    protected String[] arr_desisex;

    private RequirementInfo requirementInfo;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    public void initView() {
        mainHeadView.setMianTitle(getResources().getString(R.string.str_priview_req));
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
        requirementInfo = (RequirementInfo) intent.getSerializableExtra(Global.REQUIREMENT_INFO);
        if (requirementInfo != null) {
            act_edit_req_city_content.setText(requirementInfo.getProvince() + requirementInfo.getCity() +
                    requirementInfo.getDistrict());
            act_edit_req_cell_content.setText(requirementInfo.getCell());
            act_edit_req_qi_content.setText(requirementInfo.getCell_phase());
            act_edit_req_danyuan_content.setText(requirementInfo.getCell_unit());
            act_edit_req_dong_content.setText(requirementInfo.getCell_building());
            act_edit_req_shi_content.setText(requirementInfo.getCell_detail_number());
            act_edit_req_housearea_content.setText(requirementInfo.getHouse_area());
            act_edit_req_housetype_content.setText(TextUtils.isEmpty(requirementInfo.getHouse_type()) ? "" :
                    arr_housetype[Integer.parseInt(requirementInfo.getHouse_type())]);
            act_edit_req_decoratebudget_content.setText(TextUtils.isEmpty(requirementInfo.getTotal_price()) ? "" :
                    requirementInfo.getTotal_price());
            act_edit_req_persons_content.setText(TextUtils.isEmpty(requirementInfo.getFamily_description()) ? "" :
                    requirementInfo.getFamily_description());
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
        return R.layout.activity_priview_req;
    }
}

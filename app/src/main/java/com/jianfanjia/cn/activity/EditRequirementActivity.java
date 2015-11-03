package com.jianfanjia.cn.activity;

import android.content.Intent;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jianfanjia.cn.base.BaseAnnotationActivity;
import com.jianfanjia.cn.bean.RequirementInfo;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.fragment.XuQiuFragment;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.cutom_annotation.ReqItemFinderImp;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.cn.view.dialog.CommonDialog;
import com.jianfanjia.cn.view.dialog.DialogHelper;

import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringArrayRes;

import java.util.HashSet;
import java.util.Set;

/**
 * Description: com.jianfanjia.cn.activity
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-10-15 13:19
 */
@EActivity(R.layout.activity_edit_req)
public class EditRequirementActivity extends BaseAnnotationActivity {

    public static final int REQUIRECODE_CITY = 0x00;
    public static final int REQUIRECODE_HOUSETYPE = 0x01;
    public static final int REQUIRECODE_PERSONS = 0x02;
    public static final int REQUIRECODE_LOVESTYLE = 0x03;
    public static final int REQUIRECODE_LOVEDESISTYLE = 0x04;
    public static final int REQUIRECODE_DECORATETYPE = 0x05;
    public static final int REQUIRECODE_WORKTYPE = 0x06;
    public static final int REQUIRECODE_DESISEX = 0x07;

    public static final String REQUIRE_DATA = "require_data";
    public static final String RESPONDE_DATA = "response_data";

    public static final int TOTAL_COUNT = 16;

    private Set<String> setItems = new HashSet<>();

    @ViewById(R.id.act_edit_req_head)
    protected MainHeadView mainHeadView;
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

    @StringArrayRes(R.array.arr_lovestyle)
    protected String[] arr_lovestyle;
    @StringArrayRes(R.array.arr_housetype)
    protected String[] arr_housetype;
    @StringArrayRes(R.array.arr_love_designerstyle)
    protected String[] arr_love_designerstyle;
    @StringArrayRes(R.array.arr_worktype)
    protected String[] arr_worktype;
    @StringArrayRes(R.array.arr_decstyle)
    protected String[] arr_decstyle;
    @StringArrayRes(R.array.arr_desisex)
    protected String[] arr_desisex;

    private int totalCount;
    private int requestCode;

    private Intent gotoItem;
    private Intent gotoItemLove;
    private RequirementInfo requirementInfo;
    private CommonDialog commonDialog;

    @AfterTextChange({R.id.act_edit_req_street_content, R.id.act_edit_req_cell_content, R.id.act_edit_req_qi_content, R.id.act_edit_req_danyuan_content, R.id.act_edit_req_dong_content,
            R.id.act_edit_req_shi_content, R.id.act_edit_req_housearea_content, R.id.act_edit_req_decoratebudget_content})
    protected void afterTextChangedOnSomeTextViews(TextView tv, Editable text) {
        int viewId = tv.getId();
        String textContent = text.toString();
        if (!TextUtils.isEmpty(textContent)) {
            LogTool.d(getClass().getName() + "afterchange ", viewId + text.toString());
            switch (viewId) {
                case R.id.act_edit_req_street_content:
                    addItem("item1");
                    requirementInfo.setStreet(textContent);
                    break;
                case R.id.act_edit_req_cell_content:
                    addItem("item2");
                    requirementInfo.setCell(textContent);
                    break;
                case R.id.act_edit_req_qi_content:
                    addItem("item3");
                    requirementInfo.setCell_phase(textContent);
                    break;
                case R.id.act_edit_req_danyuan_content:
                    addItem("item4");
                    requirementInfo.setCell_unit(textContent);
                    break;
                case R.id.act_edit_req_dong_content:
                    addItem("item5");
                    requirementInfo.setCell_building(textContent);
                    break;
                case R.id.act_edit_req_shi_content:
                    addItem("item6");
                    requirementInfo.setCell_detail_number(textContent);
                    break;
                case R.id.act_edit_req_housearea_content:
                    addItem("item7");
                    requirementInfo.setHouse_area(textContent);
                    break;
                case R.id.act_edit_req_decoratebudget_content:
                    addItem("item8");
                    requirementInfo.setTotal_price(textContent);
                    break;
            }
        } else {
            switch (viewId) {
                case R.id.act_edit_req_street_content:
                    removeItem("item1");
                    break;
                case R.id.act_edit_req_cell_content:
                    removeItem("item2");
                    break;
                case R.id.act_edit_req_qi_content:
                    removeItem("item3");
                    break;
                case R.id.act_edit_req_danyuan_content:
                    removeItem("item4");
                    break;
                case R.id.act_edit_req_dong_content:
                    removeItem("item5");
                    break;
                case R.id.act_edit_req_shi_content:
                    removeItem("item6");
                    break;
                case R.id.act_edit_req_housearea_content:
                    removeItem("item7");
                    break;
                case R.id.act_edit_req_decoratebudget_content:
                    removeItem("item8");
                    break;
            }
        }
    }

    //控制确定按钮的显示
    protected void removeItem(String item) {
        if (setItems.contains(item)) {
            setItems.remove(item);
            mainHeadView.setRigthTitleEnable(false);
        }
    }

    //控制确定按钮的显示
    protected void addItem(String item) {
        if (!setItems.contains(item)) {
            setItems.add(item);
            LogTool.d(this.getClass().getName(), setItems.size() + " ==" + item);
            if (setItems.size() == TOTAL_COUNT) {
                mainHeadView.setRigthTitleEnable(true);
            }
        }
    }

    //初始化确定按钮
    protected void initItem() {
        for (int i = 0; i < TOTAL_COUNT; i++) {
            setItems.add("item" + (i + 1));
        }
        mainHeadView.setRigthTitleEnable(true);
    }


    @Click({R.id.head_back, R.id.act_edit_req_city, R.id.act_edit_req_housetype, R.id.act_edit_req_decoratetype,
            R.id.act_edit_req_lovestyle, R.id.act_edit_req_persons, R.id.act_edit_req_lovedesistyle, R.id.act_edit_req_lovedesisex, R.id.act_edit_req_work_type})
    protected void back(View clickView) {
        int viewId = clickView.getId();
        switch (viewId) {
            case R.id.head_back:
                comeMainActivity();
                break;
            case R.id.act_edit_req_city:
                gotoItem.putExtra(REQUIRE_DATA, REQUIRECODE_CITY);
                startActivityForResult(gotoItem, REQUIRECODE_CITY);
                break;
            case R.id.act_edit_req_lovedesistyle:
                gotoItem.putExtra(REQUIRE_DATA, REQUIRECODE_LOVEDESISTYLE);
                startActivityForResult(gotoItem, REQUIRECODE_LOVEDESISTYLE);
                break;
            case R.id.act_edit_req_lovestyle:
                gotoItemLove.putExtra(REQUIRE_DATA, REQUIRECODE_LOVESTYLE);
                startActivityForResult(gotoItemLove, REQUIRECODE_LOVESTYLE);
                break;
            case R.id.act_edit_req_persons:
                gotoItem.putExtra(REQUIRE_DATA, REQUIRECODE_PERSONS);
                startActivityForResult(gotoItem, REQUIRECODE_PERSONS);
                break;
            case R.id.act_edit_req_decoratetype:
                gotoItem.putExtra(REQUIRE_DATA, REQUIRECODE_DECORATETYPE);
                startActivityForResult(gotoItem, REQUIRECODE_DECORATETYPE);
                break;
            case R.id.act_edit_req_housetype:
                gotoItem.putExtra(REQUIRE_DATA, REQUIRECODE_HOUSETYPE);
                startActivityForResult(gotoItem, REQUIRECODE_HOUSETYPE);
                break;
            case R.id.act_edit_req_work_type:
                gotoItem.putExtra(REQUIRE_DATA, REQUIRECODE_WORKTYPE);
                startActivityForResult(gotoItem, REQUIRECODE_WORKTYPE);
                break;
            case R.id.act_edit_req_lovedesisex:
                gotoItem.putExtra(REQUIRE_DATA, REQUIRECODE_DESISEX);
                startActivityForResult(gotoItem, REQUIRECODE_DESISEX);
                break;
        }
    }

    @Click(R.id.head_right_title)
    protected void confirm() {
        if (requestCode == XuQiuFragment.REQUESTCODE_PUBLISH_REQUIREMENT) {
            JianFanJiaClient.add_Requirement(this, requirementInfo, this, this);
        } else {
            JianFanJiaClient.update_Requirement(this, requirementInfo, this, this);
        }
    }

    @Override
    public void loadSuccess(Object data) {
        super.loadSuccess(data);
        showSuccessDialog();
        LogTool.d(getClass().getName(), data.toString());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                commonDialog.dismiss();
                /*setResult(RESULT_OK);
                finish();*/
                comeMainActivity();
            }
        }, 2000);
    }

    private void comeMainActivity() {
        Intent intent = new Intent(EditRequirementActivity.this, MainActivity.class);
        intent.putExtra(Constant.TAB_POSITION, Constant.MANAGE);
        startActivity(intent);
        finish();
    }

    private void showSuccessDialog() {
        commonDialog = DialogHelper
                .getPinterestDialogCancelable(this);
        if (requestCode == XuQiuFragment.REQUESTCODE_PUBLISH_REQUIREMENT) {
            commonDialog.setTitle("发布成功");
            commonDialog.setMessage("您的需求发布成功啦！");
        } else {
            commonDialog.setTitle("更新成功");
            commonDialog.setMessage("您的需求更新成功啦！");
        }
        commonDialog.show();
    }

    @AfterViews
    protected void setMainHeadView() {
        mainHeadView.setMianTitle(getResources().getString(R.string.str_edit_req));
        mainHeadView.setRightTitle(getResources().getString(R.string.finish));
        mainHeadView.setRigthTitleEnable(false);

        gotoItem = new Intent(this, EditRequirementItemActivity_.class);
        gotoItemLove = new Intent(this, EditRequirementLovestyleActivity_.class);

        initData();
    }

    private void initData() {

        Intent intent = getIntent();
        requirementInfo = (RequirementInfo) intent.getSerializableExtra(Global.REQUIREMENT_INFO);
        if (requirementInfo != null) {
            act_edit_req_city_content.setText(requirementInfo.getProvince() + requirementInfo.getCity() + requirementInfo.getDistrict());
            act_edit_req_street_content.setText(requirementInfo.getStreet());
            act_edit_req_cell_content.setText(requirementInfo.getCell());
            act_edit_req_qi_content.setText(requirementInfo.getCell_phase());
            act_edit_req_danyuan_content.setText(requirementInfo.getCell_unit());
            act_edit_req_dong_content.setText(requirementInfo.getCell_building());
            act_edit_req_shi_content.setText(requirementInfo.getCell_detail_number());
            act_edit_req_decoratetype_content.setText(arr_decstyle[Integer.parseInt(requirementInfo.getDec_type())]);
            act_edit_req_housearea_content.setText(requirementInfo.getHouse_area());
            act_edit_req_housetype_content.setText(arr_housetype[Integer.parseInt(requirementInfo.getHouse_type())]);
            act_edit_req_decoratebudget_content.setText(requirementInfo.getTotal_price());
            act_edit_req_persons_content.setText(requirementInfo.getFamily_description());
            act_edit_req_lovestyle_content.setText(arr_lovestyle[Integer.parseInt(requirementInfo.getDec_style())]);
            act_edit_req_lovedesistyle_content.setText(arr_love_designerstyle[Integer.parseInt(requirementInfo.getCommunication_type())]);
            act_edit_req_lovedesisex_content.setText(arr_desisex[Integer.parseInt(requirementInfo.getPrefer_sex())]);
            act_edit_req_work_type_content.setText(arr_worktype[Integer.parseInt(requirementInfo.getWork_type())]);
            initItem();
            requestCode = XuQiuFragment.REQUESTCODE_EDIT_REQUIREMENT;
        } else {
            requirementInfo = new RequirementInfo();
            act_edit_req_city_content.setText(getString(R.string.city_tip));
            requestCode = XuQiuFragment.REQUESTCODE_PUBLISH_REQUIREMENT;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (data != null) {
            ReqItemFinderImp.ItemMap itemMap = (ReqItemFinderImp.ItemMap) data.getSerializableExtra(RESPONDE_DATA);
            switch (requestCode) {
                case REQUIRECODE_CITY:
                    act_edit_req_city_content.setText(requirementInfo.getProvince() + requirementInfo.getCity() + itemMap.value);
                    addItem("item9");
                    requirementInfo.setDistrict(itemMap.value);
                    break;
                case REQUIRECODE_LOVEDESISTYLE:
                    act_edit_req_lovedesistyle_content.setText(itemMap.value);
                    addItem("item10");
                    requirementInfo.setCommunication_type(itemMap.key);
                    break;
                case REQUIRECODE_LOVESTYLE:
                    act_edit_req_lovestyle_content.setText(itemMap.value);
                    addItem("item111");
                    requirementInfo.setDec_style(itemMap.key);
                    break;
                case REQUIRECODE_PERSONS:
                    act_edit_req_persons_content.setText(itemMap.value);
                    addItem("item12");
                    requirementInfo.setFamily_description(itemMap.value);
                    break;
                case REQUIRECODE_DECORATETYPE:
                    act_edit_req_decoratetype_content.setText(itemMap.value);
                    addItem("item13");
                    requirementInfo.setDec_type(itemMap.key);
                    break;
                case REQUIRECODE_HOUSETYPE:
                    act_edit_req_housetype_content.setText(itemMap.value);
                    addItem("item114");
                    requirementInfo.setHouse_type(itemMap.key);
                    break;
                case REQUIRECODE_WORKTYPE:
                    act_edit_req_work_type_content.setText(itemMap.value);
                    addItem("item15");
                    requirementInfo.setWork_type(itemMap.key);
                    break;
                case REQUIRECODE_DESISEX:
                    act_edit_req_lovedesisex_content.setText(itemMap.value);
                    addItem("item16");
                    makeTextLong(itemMap.key);
                    requirementInfo.setPrefer_sex(itemMap.key);
                    break;
            }
        }

    }
}

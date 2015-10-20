package com.jianfanjia.cn.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.RequirementInfo;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.cutom_annotation.ReqItemFinderImp;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.MainHeadView;

import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.HashSet;
import java.util.Set;

/**
 * Description: com.jianfanjia.cn.activity
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-10-15 13:19
 */
@EActivity
public class EditRequirementActivity extends BaseActivity {

    public static final int REQUIRECODE_CITY = 0x00;
    public static final int REQUIRECODE_HOUSETYPE = 0x01;
    public static final int REQUIRECODE_PERSONS = 0x02;
    public static final int REQUIRECODE_LOVESTYLE = 0x03;
    public static final int REQUIRECODE_LOVEDESISTYLE = 0x04;
    public static final int REQUIRECODE_DECORATETYPE = 0x05;

    public static final String REQUIRE_DATA = "require_data";
    public static final String RESPONDE_DATA = "response_data";

    public static final int TOTAL_COUNT = 14;

    private Set<String> setItems = new HashSet<>();

    @ViewById(R.id.act_edit_req_head)
    protected MainHeadView mainHeadView;
    @ViewById
    protected TextView act_edit_req_city_content;//所在城市
    @ViewById
    protected TextView act_edit_req_housetype_content;//户型
    @ViewById
    protected EditText act_edit_req_persons_content;//计划常住人口
    @ViewById
    protected TextView act_edit_req_lovestyle_content;//风格喜好
    @ViewById
    protected TextView act_edit_req_lovedesistyle_content;//偏好设计师类型
    @ViewById
    protected TextView act_edit_req_decoratetype_content;//装修类型
    @ViewById
    protected EditText act_edit_req_street_content;//所在街道
    @ViewById
    protected EditText act_edit_req_housearea_content;//装修面积
    @ViewById
    protected EditText act_edit_req_decoratebudget_content;//装修预算

    private Intent gotoItem;
    private Intent gotoItemLove;
    private RequirementInfo requirementInfo;

    @AfterTextChange({R.id.act_edit_req_street_content, R.id.act_edit_req_cell_content, R.id.act_edit_req_qi_content, R.id.act_edit_req_danyuan_content, R.id.act_edit_req_dong_content,
            R.id.act_edit_req_shi_content, R.id.act_edit_req_housearea_content, R.id.act_edit_req_decoratebudget_content, R.id.act_edit_req_persons_content})
    protected void afterTextChangedOnSomeTextViews(TextView tv, Editable text) {
        int viewId = tv.getId();
        String textContent = text.toString();
        if (!TextUtils.isEmpty(textContent)) {
            LogTool.d(getClass().getName() + "afterchange ", viewId + text.toString());
            switch (viewId) {
                case R.id.act_edit_req_street_content:
                    changeConfirmStatus("street");
                    requirementInfo.setStreet(textContent);
                    break;
                case R.id.act_edit_req_cell_content:
                    changeConfirmStatus("cell");
                    requirementInfo.setCell(textContent);
                    break;
                case R.id.act_edit_req_qi_content:
                    changeConfirmStatus("qi");
                    requirementInfo.setCell_phase(textContent);
                    break;
                case R.id.act_edit_req_danyuan_content:
                    changeConfirmStatus("danyuan");
                    requirementInfo.setCell_unit(textContent);
                    break;
                case R.id.act_edit_req_dong_content:
                    changeConfirmStatus("dong");
                    requirementInfo.setCell_building(textContent);
                    break;
                case R.id.act_edit_req_shi_content:
                    changeConfirmStatus("shi");
                    requirementInfo.setCell_detail_number(textContent);
                    break;
                case R.id.act_edit_req_housearea_content:
                    changeConfirmStatus("housearea");
                    requirementInfo.setHouse_area(textContent);
                    break;
                case R.id.act_edit_req_decoratebudget_content:
                    changeConfirmStatus("decoratebudget");
                    requirementInfo.setTotal_price(textContent);
                    break;
                case R.id.act_edit_req_persons_content:
                    changeConfirmStatus("persons");
                    requirementInfo.setFamily_description(textContent);
                    break;
            }
        } else {
            switch (viewId) {
                case R.id.act_edit_req_street_content:
                    setItems.remove("street");
                    mainHeadView.setRigthTitleEnable(false);
                    break;
                case R.id.act_edit_req_cell_content:
                    setItems.remove("cell");
                    mainHeadView.setRigthTitleEnable(false);
                    break;
                case R.id.act_edit_req_qi_content:
                    setItems.remove("qi");
                    mainHeadView.setRigthTitleEnable(false);
                    break;
                case R.id.act_edit_req_danyuan_content:
                    setItems.remove("danyuan");
                    mainHeadView.setRigthTitleEnable(false);
                    break;
                case R.id.act_edit_req_dong_content:
                    setItems.remove("dong");
                    mainHeadView.setRigthTitleEnable(false);
                    break;
                case R.id.act_edit_req_shi_content:
                    setItems.remove("shi");
                    mainHeadView.setRigthTitleEnable(false);
                    break;
                case R.id.act_edit_req_housearea_content:
                    setItems.remove("housearea");
                    mainHeadView.setRigthTitleEnable(false);
                    break;
                case R.id.act_edit_req_decoratebudget_content:
                    setItems.remove("decoratebudget");
                    mainHeadView.setRigthTitleEnable(false);
                    break;
                case R.id.act_edit_req_persons_content:
                    setItems.remove("persons");
                    mainHeadView.setRigthTitleEnable(false);
                    break;
            }
        }
    }

    protected void changeConfirmStatus(String item) {
        setItems.add(item);
        LogTool.d(this.getClass().getName(), setItems.size() + " ==" + item);
        if (setItems.size() == TOTAL_COUNT) {
            mainHeadView.setRigthTitleEnable(true);
        }
    }

    @Click({R.id.head_back, R.id.act_edit_req_city, R.id.act_edit_req_housetype, R.id.act_edit_req_decoratetype,
            R.id.act_edit_req_lovestyle, R.id.act_edit_req_lovedesistyle})
    protected void back(View clickView) {
        int viewId = clickView.getId();
        switch (viewId) {
            case R.id.head_back:
                finish();
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
            case R.id.act_edit_req_decoratetype:
                gotoItem.putExtra(REQUIRE_DATA, REQUIRECODE_DECORATETYPE);
                startActivityForResult(gotoItem, REQUIRECODE_DECORATETYPE);
                break;
            case R.id.act_edit_req_housetype:
                gotoItem.putExtra(REQUIRE_DATA, REQUIRECODE_HOUSETYPE);
                startActivityForResult(gotoItem, REQUIRECODE_HOUSETYPE);
                break;
        }
    }

    @Click(R.id.head_right_title)
    protected void confirm() {
        makeTextLong("确定");
        JianFanJiaClient.add_Requirement(this,requirementInfo,this,this);
    }

    @Override
    public void loadSuccess(Object data) {
        super.loadSuccess(data);
        LogTool.d(getClass().getName(),data.toString());
        makeTextLong("发布成功");
    }

    @AfterViews
    protected void setMainHeadView() {
        mainHeadView.setMianTitle(getResources().getString(R.string.str_edit_req));
        mainHeadView.setRightTitle(getResources().getString(R.string.confirm));
        mainHeadView.setRigthTitleEnable(false);

        initData();
    }

    private void initData() {

        Intent intent = getIntent();
        requirementInfo = (RequirementInfo)intent.getSerializableExtra(Global.REQUIREMENT_INFO);
        if(requirementInfo != null){

        }else{
            requirementInfo = new RequirementInfo();
        }

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_edit_req;
    }

    @Override
    public void initView() {
        gotoItem = new Intent(this, EditRequirementItemActivity_.class);
        gotoItemLove = new Intent(this, EditRequirementLovestyleActivity_.class);
    }

    @Override
    public void setListener() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (data != null) {
            ReqItemFinderImp.ItemMap itemMap = (ReqItemFinderImp.ItemMap) data.getSerializableExtra(RESPONDE_DATA);
            makeTextLong(itemMap.key);
            switch (requestCode) {
                case REQUIRECODE_CITY:
                    act_edit_req_city_content.setText(itemMap.value);
                    changeConfirmStatus("city");
                    requirementInfo.setDistrict(itemMap.value);
                    break;
                case REQUIRECODE_LOVEDESISTYLE:
                    act_edit_req_lovedesistyle_content.setText(itemMap.value);
                    changeConfirmStatus("lovedesistyle");
                    requirementInfo.setCommunication_type(itemMap.key);
                    break;
                case REQUIRECODE_LOVESTYLE:
                    act_edit_req_lovestyle_content.setText(itemMap.value);
                    changeConfirmStatus("lovestyle");
                    requirementInfo.setDec_style(itemMap.key);
                    break;
                case REQUIRECODE_DECORATETYPE:
                    act_edit_req_decoratetype_content.setText(itemMap.value);
                    changeConfirmStatus("decoratetype");
                    requirementInfo.setDec_style(itemMap.key);
                    break;
                case REQUIRECODE_HOUSETYPE:
                    act_edit_req_housetype_content.setText(itemMap.value);
                    changeConfirmStatus("housetype");
                    requirementInfo.setHouse_type(itemMap.key);
                    break;
            }
        }

    }
}

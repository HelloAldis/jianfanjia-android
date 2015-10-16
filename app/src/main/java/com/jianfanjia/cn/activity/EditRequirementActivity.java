package com.jianfanjia.cn.activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.interf.cutom_annotation.ReqItemFinderImp;
import com.jianfanjia.cn.view.MainHeadView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

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
    protected EditText act_edit_req_street_content;//所在街道
    @ViewById
    protected EditText act_edit_req_housearea_content;//装修面积
    @ViewById
    protected EditText act_edit_req_decoratebudget_content;//装修预算

    private Intent gotoItem;
    private Intent gotoItemLove;

    @Click({R.id.head_back, R.id.act_edit_req_city, R.id.act_edit_req_housetype, R.id.act_edit_req_decoratetype,
            R.id.act_edit_req_persons, R.id.act_edit_req_lovestyle, R.id.act_edit_req_lovedesistyle})
    protected void back(View clickView) {
        int resId = clickView.getId();
        switch (resId) {
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
            case R.id.act_edit_req_persons:
                gotoItem.putExtra(REQUIRE_DATA, REQUIRECODE_PERSONS);
                startActivityForResult(gotoItem, REQUIRECODE_PERSONS);
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

    @AfterViews
    protected void setMainHeadView() {
        mainHeadView.setMianTitle(getResources().getString(R.string.str_edit_req));
        mainHeadView.setRightTitle(getResources().getString(R.string.confirm));
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
                    break;
                case REQUIRECODE_LOVEDESISTYLE:
                    act_edit_req_lovedesistyle_content.setText(itemMap.value);
                    break;
                case REQUIRECODE_PERSONS:
                    act_edit_req_persons_content.setText(itemMap.value);
                    break;
                case REQUIRECODE_LOVESTYLE:
                    act_edit_req_lovestyle_content.setText(itemMap.value);
                    break;
                case REQUIRECODE_DECORATETYPE:
                    act_edit_req_decoratetype_content.setText(itemMap.value);
                    break;
                case REQUIRECODE_HOUSETYPE:
                    act_edit_req_housetype_content.setText(itemMap.value);
                    break;
            }
        }

    }
}

package com.jianfanjia.cn.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ListView;

import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.view.MainHeadView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringArrayRes;

/**
 * Description: com.jianfanjia.cn.activity
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-10-15 13:19
 */
@EActivity
public class EditRequirementItemActivity extends BaseActivity {

    @ViewById(R.id.act_edit_req_item_head)
    protected MainHeadView mainHeadView;

    @ViewById(R.id.act_edit_req_item_listview)
    protected ListView edit_req_item_listview;

    @StringArrayRes(R.array.arr_district)
    protected String[] arr_district;

    @StringArrayRes(R.array.arr_decoratestyle)
    protected String[] arr_decoratestyle;

    @StringArrayRes(R.array.arr_housetype)
    protected String[] arr_housetype;

    @StringArrayRes(R.array.arr_person)
    protected String[] arr_person;

    @StringArrayRes(R.array.arr_love_designerstyle)
    protected String[] arr_love_designerstyle;

    @AfterViews
    protected void setMainHeadView() {
        mainHeadView.setRightTitleVisable(View.GONE);
    }

    @AfterViews
    protected void setView(){
        Intent data = getIntent();

    }

    @Click({R.id.head_back})
    protected void back(View clickView) {
        int resId = clickView.getId();
        switch (resId) {
            case R.id.head_back:
                finish();
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_edit_req_item;
    }

    @Override
    public void initView() {
    }

    @Override
    public void setListener() {

    }
}

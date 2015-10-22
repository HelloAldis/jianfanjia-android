package com.jianfanjia.cn.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ListView;

import com.jianfanjia.cn.adapter.RequirementItemAdapter;
import com.jianfanjia.cn.base.BaseAnnotationActivity;
import com.jianfanjia.cn.interf.cutom_annotation.ReqItemFinderImp;
import com.jianfanjia.cn.view.MainHeadView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import static com.jianfanjia.cn.activity.EditRequirementActivity.REQUIRECODE_CITY;
import static com.jianfanjia.cn.activity.EditRequirementActivity.REQUIRECODE_DECORATETYPE;
import static com.jianfanjia.cn.activity.EditRequirementActivity.REQUIRECODE_DESISEX;
import static com.jianfanjia.cn.activity.EditRequirementActivity.REQUIRECODE_HOUSETYPE;
import static com.jianfanjia.cn.activity.EditRequirementActivity.REQUIRECODE_LOVEDESISTYLE;
import static com.jianfanjia.cn.activity.EditRequirementActivity.REQUIRECODE_PERSONS;
import static com.jianfanjia.cn.activity.EditRequirementActivity.REQUIRECODE_WORKTYPE;
import static com.jianfanjia.cn.activity.EditRequirementActivity.REQUIRE_DATA;

/**
 * Description: com.jianfanjia.cn.activity
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-10-15 13:19
 */
@EActivity(R.layout.activity_edit_req_item)
public class EditRequirementItemActivity extends BaseAnnotationActivity {

    //用来记录是展示那个列表
    private int requestCode;

    @ViewById(R.id.act_edit_req_item_head)
    protected MainHeadView mainHeadView;

    @ViewById(R.id.act_edit_req_item_listview)
    protected ListView edit_req_item_listview;

    @Bean
    protected RequirementItemAdapter requirementItemAdapter;

    @AfterViews
    protected void setView() {
        Intent data = getIntent();
        requestCode = data.getIntExtra(REQUIRE_DATA, 0);
        showHead(requestCode);

        edit_req_item_listview.setAdapter(requirementItemAdapter);
        requirementItemAdapter.changeShow(requestCode);
    }

    /**
     * 根据requestcode动态展示头部显示的文字
     *
     * @param requestcode
     */
    private void showHead(int requestcode) {
        mainHeadView.setRightTitleVisable(View.GONE);
        switch (requestcode) {
            case REQUIRECODE_CITY:
                mainHeadView.setMianTitle(getString(R.string.str_district));
                break;
            case REQUIRECODE_HOUSETYPE:
                mainHeadView.setMianTitle(getString(R.string.str_housetype));
                break;
            case REQUIRECODE_PERSONS:
                mainHeadView.setMianTitle(getString(R.string.str_persons));
                break;
            case REQUIRECODE_LOVEDESISTYLE:
                mainHeadView.setMianTitle(getString(R.string.str_lovedesistyle));
                break;
            case REQUIRECODE_DECORATETYPE:
                mainHeadView.setMianTitle(getString(R.string.str_decoratetype));
                break;
            case REQUIRECODE_WORKTYPE:
                mainHeadView.setMianTitle(getResources().getString(R.string.str_work_type));
                break;
            case REQUIRECODE_DESISEX:
                mainHeadView.setMianTitle(getString(R.string.str_lovedesisex));
                break;
        }

    }

    @ItemClick(R.id.act_edit_req_item_listview)
    void personListItemClicked(ReqItemFinderImp.ItemMap itemMap) {
        Intent data = new Intent(this, EditRequirementActivity_.class);
        data.putExtra(EditRequirementActivity.RESPONDE_DATA, itemMap);
        setResult(RESULT_OK, data);
        finish();
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

}

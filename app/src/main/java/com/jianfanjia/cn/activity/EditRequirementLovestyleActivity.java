package com.jianfanjia.cn.activity;

import android.content.Intent;
import android.view.View;
import android.widget.GridView;

import com.jianfanjia.cn.adapter.RequirementItemLoveStyleAdapter;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.interf.cutom_annotation.ReqItemFinderImp;
import com.jianfanjia.cn.view.MainHeadView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import static com.jianfanjia.cn.activity.EditRequirementActivity.REQUIRE_DATA;

/**
 * Description: com.jianfanjia.cn.activity
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-10-15 13:19
 */
@EActivity
public class EditRequirementLovestyleActivity extends BaseActivity{

    //用来记录是展示那个列表
    private int requestCode;

    @ViewById(R.id.act_edit_req_lovestyle_gridview)
    GridView gridView;

    @ViewById(R.id.act_edit_req_lovastyle_head)
    MainHeadView mainHeadView;

    @Bean
    RequirementItemLoveStyleAdapter requirementItemLoveStyleAdapter;

    @AfterViews
    void setView(){
        mainHeadView.setMianTitle(getString(R.string.style));
        Intent data = getIntent();
        requestCode = data.getIntExtra(REQUIRE_DATA, 0);

        gridView.setAdapter(requirementItemLoveStyleAdapter);
        requirementItemLoveStyleAdapter.changeShow(requestCode);
    }

    @ItemClick(R.id.act_edit_req_lovestyle_gridview)
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

    @Override
    public int getLayoutId() {
        return R.layout.activity_edit_req_lovestyle;
    }

    @Override
    public void initView() {

    }

    @Override
    public void setListener() {

    }
}

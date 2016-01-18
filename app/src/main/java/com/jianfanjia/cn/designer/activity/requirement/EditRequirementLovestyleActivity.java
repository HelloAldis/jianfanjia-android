package com.jianfanjia.cn.designer.activity.requirement;

import android.content.Intent;
import android.view.View;
import android.widget.GridView;

import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.adapter.RequirementItemLoveStyleAdapter;
import com.jianfanjia.cn.designer.base.BaseAnnotationActivity;
import com.jianfanjia.cn.designer.config.Global;
import com.jianfanjia.cn.designer.interf.cutom_annotation.ReqItemFinderImp;
import com.jianfanjia.cn.designer.view.MainHeadView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

/**
 * Description: com.jianfanjia.cn.activity
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-10-15 13:19
 */
@EActivity(R.layout.activity_edit_req_lovestyle)
public class EditRequirementLovestyleActivity extends BaseAnnotationActivity{

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
        requestCode = data.getIntExtra(Global.REQUIRE_DATA, 0);

        gridView.setAdapter(requirementItemLoveStyleAdapter);
        requirementItemLoveStyleAdapter.changeShow(requestCode);
    }

    @ItemClick(R.id.act_edit_req_lovestyle_gridview)
    void personListItemClicked(ReqItemFinderImp.ItemMap itemMap) {
        Intent data = new Intent(this, UpdateRequirementActivity_.class);
        data.putExtra(Global.RESPONSE_DATA, itemMap);
        setResult(RESULT_OK, data);
        appManager.finishActivity(this);
    }

    @Click({R.id.head_back})
    protected void back(View clickView) {
        int resId = clickView.getId();
        switch (resId) {
            case R.id.head_back:
                appManager.finishActivity(this);
                break;
        }
    }

}

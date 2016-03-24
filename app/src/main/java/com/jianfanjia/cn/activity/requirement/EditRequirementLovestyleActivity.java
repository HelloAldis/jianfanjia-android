package com.jianfanjia.cn.activity.requirement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnItemClick;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.SwipeBackActivity;
import com.jianfanjia.cn.adapter.RequirementItemLoveStyleAdapter;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.interf.cutom_annotation.ReqItemFinderImp;
import com.jianfanjia.cn.view.MainHeadView;

/**
 * Description: com.jianfanjia.cn.activity
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-10-15 13:19
 */
public class EditRequirementLovestyleActivity extends SwipeBackActivity {

    //用来记录是展示那个列表
    private int requestCode;

    @Bind(R.id.act_edit_req_lovestyle_gridview)
    GridView gridView;

    @Bind(R.id.act_edit_req_lovastyle_head)
    MainHeadView mainHeadView;

    RequirementItemLoveStyleAdapter requirementItemLoveStyleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    public void initView() {
        mainHeadView.setMianTitle(getString(R.string.style));
        Intent data = getIntent();
        requestCode = data.getIntExtra(Global.REQUIRE_DATA, 0);

        requirementItemLoveStyleAdapter = new RequirementItemLoveStyleAdapter(this);
        gridView.setAdapter(requirementItemLoveStyleAdapter);
        requirementItemLoveStyleAdapter.changeShow(requestCode);
    }


    @OnItemClick(R.id.act_edit_req_lovestyle_gridview)
    void personListItemClicked(ReqItemFinderImp.ItemMap itemMap) {
        Intent data = new Intent(this, UpdateRequirementActivity_.class);
        data.putExtra(Global.RESPONSE_DATA, itemMap);
        setResult(RESULT_OK, data);
        appManager.finishActivity(this);
    }

    @OnClick({R.id.head_back_layout})
    protected void back(View clickView) {
        int resId = clickView.getId();
        switch (resId) {
            case R.id.head_back:
                appManager.finishActivity(this);
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_edit_req_lovestyle;
    }
}

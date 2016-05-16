package com.jianfanjia.cn.ui.activity.requirement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.ui.adapter.RequirementItemLoveStyleAdapter;
import com.jianfanjia.cn.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.constant.IntentConstant;
import com.jianfanjia.cn.view.MainHeadView;

/**
 * Description: com.jianfanjia.cn.activity
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-10-15 13:19
 */
public class EditRequirementLovestyleActivity extends BaseSwipeBackActivity {

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
        requestCode = data.getIntExtra(IntentConstant.REQUIRE_DATA, 0);

        requirementItemLoveStyleAdapter = new RequirementItemLoveStyleAdapter(this);
        gridView.setAdapter(requirementItemLoveStyleAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent data = new Intent(EditRequirementLovestyleActivity.this, UpdateRequirementActivity.class);
                data.putExtra(IntentConstant.RESPONSE_DATA, requirementItemLoveStyleAdapter.getItemMaps().get(position));
                setResult(RESULT_OK, data);
                appManager.finishActivity(EditRequirementLovestyleActivity.this);
            }
        });
        requirementItemLoveStyleAdapter.changeShow(requestCode);
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

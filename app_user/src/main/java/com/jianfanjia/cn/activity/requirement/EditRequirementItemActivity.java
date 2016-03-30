package com.jianfanjia.cn.activity.requirement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.adapter.RequirementItemAdapter;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.view.MainHeadView;

/**
 * Description: com.jianfanjia.cn.activity
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-10-15 13:19
 */
public class EditRequirementItemActivity extends BaseSwipeBackActivity {

    //用来记录是展示那个列表
    private int requestCode;

    @Bind(R.id.act_edit_req_item_head)
    protected MainHeadView mainHeadView;

    @Bind(R.id.act_edit_req_item_listview)
    protected ListView edit_req_item_listview;

    protected RequirementItemAdapter requirementItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    public void initView() {
        Intent data = getIntent();
        requestCode = data.getIntExtra(Global.REQUIRE_DATA, 0);
        showHead(requestCode);

        requirementItemAdapter = new RequirementItemAdapter(this);
        edit_req_item_listview.setAdapter(requirementItemAdapter);
        edit_req_item_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent data = new Intent(EditRequirementItemActivity.this, UpdateRequirementActivity.class);
                data.putExtra(Global.RESPONSE_DATA, requirementItemAdapter.getItemMaps().get(position));
                setResult(RESULT_OK, data);
                appManager.finishActivity(EditRequirementItemActivity.this);
            }
        });
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
            case Constant.REQUIRECODE_CITY:
                mainHeadView.setMianTitle(getString(R.string.str_district));
                break;
            case Constant.REQUIRECODE_HOUSETYPE:
                mainHeadView.setMianTitle(getString(R.string.str_housetype));
                break;
            case Constant.REQUIRECODE_PERSONS:
                mainHeadView.setMianTitle(getString(R.string.str_persons));
                break;
            case Constant.REQUIRECODE_LOVEDESISTYLE:
                mainHeadView.setMianTitle(getString(R.string.str_lovedesistyle));
                break;
            case Constant.REQUIRECODE_BUSI_DECORATETYPE:
                mainHeadView.setMianTitle(getString(R.string.str_businessdecoratetype));
                break;
            case Constant.REQUIRECODE_WORKTYPE:
                mainHeadView.setMianTitle(getResources().getString(R.string.str_work_type));
                break;
            case Constant.REQUIRECODE_DESISEX:
                mainHeadView.setMianTitle(getString(R.string.str_lovedesisex));
                break;
        }

    }

    @OnClick({R.id.head_back_layout})
    protected void back(View clickView) {
        int resId = clickView.getId();
        switch (resId) {
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_edit_req_item;
    }
}

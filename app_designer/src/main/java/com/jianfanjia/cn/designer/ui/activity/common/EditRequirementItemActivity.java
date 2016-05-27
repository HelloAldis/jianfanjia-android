package com.jianfanjia.cn.designer.ui.activity.common;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.designer.config.Constant;
import com.jianfanjia.cn.designer.config.Global;
import com.jianfanjia.cn.designer.ui.adapter.RequirementItemAdapter;
import com.jianfanjia.cn.designer.view.MainHeadView;

/**
 * Description: com.jianfanjia.cn.activity
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-10-15 13:19
 */
public class EditRequirementItemActivity extends BaseSwipeBackActivity {

    public static final String CURRENT_CHOOSED_VALUE = "current_choosed_value";

    //用来记录是展示那个列表
    private int requestCode;

    private String  currentChooseValue;

    @Bind(R.id.act_edit_req_item_head)
    protected MainHeadView mainHeadView;

    @Bind(R.id.act_edit_req_item_listview)
    protected ListView edit_req_item_listview;

    protected RequirementItemAdapter requirementItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
            currentChooseValue = bundle.getString(CURRENT_CHOOSED_VALUE);
        }
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
                Intent data = getIntent();
                data.putExtra(Global.RESPONSE_DATA, requirementItemAdapter.getItemMaps().get(position));
                setResult(RESULT_OK, data);
                appManager.finishActivity(EditRequirementItemActivity.this);
            }
        });
        requirementItemAdapter.changeShow(requestCode,currentChooseValue);
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
            case Constant.REQUIRECODE_DECTYPE:
                mainHeadView.setMianTitle(getString(R.string.str_decoratetype));
                break;
            case Constant.REQUIRECODE_BANK:
                mainHeadView.setMianTitle(getString(R.string.bank));
                break;
            case Constant.REQUIRECODE_GOODAT_WORKOFTYPE:
                mainHeadView.setMianTitle(getString(R.string.goodat_type));
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

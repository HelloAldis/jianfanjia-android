package com.jianfanjia.cn.designer.ui.activity.my_info_auth;

import android.os.Bundle;
import android.view.View;

import butterknife.Bind;
import butterknife.OnClick;

import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.designer.config.Constant;
import com.jianfanjia.cn.designer.config.Global;
import com.jianfanjia.cn.designer.tools.IntentUtil;
import com.jianfanjia.cn.designer.ui.activity.common.ChooseItemActivity;
import com.jianfanjia.cn.designer.view.MainHeadView;

/**
 * Description: com.jianfanjia.cn.designer.ui.activity.my_info_auth
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-05-18 11:32
 */
public class DesignerReceiveInfoActivity extends BaseSwipeBackActivity {

    @Bind(R.id.receive_business_head_layout)
    MainHeadView mMainHeadView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getDataFromIntent();
        initView();
    }

    private void initView() {
        initMainView();

    }

    private void initMainView() {
        mMainHeadView.setMianTitle(getString(R.string.receive_business_info));
        mMainHeadView.setRightTitle(getString(R.string.commit));
        mMainHeadView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                updateDesignerTeamInfo(mTeam);
            }
        });
//        setMianHeadRightTitleEnable();
    }

    @OnClick({R.id.act_edit_req_dectype})
    protected void click(View view){
        switch (view.getId()){
            case R.id.act_edit_req_dectype:
                Bundle personBundle = new Bundle();
                personBundle.putInt(Global.REQUIRE_DATA, Constant.REQUIRECODE_DECTYPE);
                personBundle.putInt(ChooseItemActivity.CURRENT_CHOOSED_TYPE,ChooseItemActivity.CHOOSE_TYPE_MULTIPLE);
                IntentUtil.startActivityForResult(this, ChooseItemActivity.class, personBundle, Constant.REQUIRECODE_DECTYPE);
                break;
        }
    }

    private void getDataFromIntent() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_designer_receive_business;
    }
}

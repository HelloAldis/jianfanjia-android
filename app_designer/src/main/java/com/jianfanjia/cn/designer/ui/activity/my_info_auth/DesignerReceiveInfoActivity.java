package com.jianfanjia.cn.designer.ui.activity.my_info_auth;

import android.os.Bundle;
import android.view.View;

import butterknife.Bind;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.base.BaseSwipeBackActivity;
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

    private void getDataFromIntent() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_designer_receive_business;
    }
}

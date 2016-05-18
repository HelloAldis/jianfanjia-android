package com.jianfanjia.cn.designer.ui.activity.my_info_auth;

import android.os.Bundle;
import android.view.View;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.designer.ui.activity.my_info_auth.base_info.BaseInfoAuthActicity;
import com.jianfanjia.cn.designer.ui.activity.my_info_auth.identity_info.DesignerIdentityAuthActivity;
import com.jianfanjia.cn.designer.ui.activity.my_info_auth.product_info.DesignerProductAuthActivity;
import com.jianfanjia.cn.designer.ui.activity.my_info_auth.team_info.DesignerTeamAuthActivity;
import com.jianfanjia.cn.designer.view.MainHeadView;

/**
 * Description: com.jianfanjia.cn.designer.ui.activity.my_info_auth
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-05-17 15:22
 */
public class DesignerInfoAuthActivity extends BaseSwipeBackActivity {

    @Bind(R.id.designer_auth_head_layout)
    MainHeadView mMainHeadView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        initMainView();
    }

    private void initMainView() {
        mMainHeadView.setMianTitle(getString(R.string.designer_auth));
    }

    @OnClick({R.id.head_back_layout, R.id.base_info_auth_layout, R.id.identity_auth_layout, R.id.product_auth_layout,
            R.id.process_team_auth_layout, R.id.email_auth_layout})
    protected void click(View view) {
        switch (view.getId()) {
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
            case R.id.base_info_auth_layout:
                startActivity(BaseInfoAuthActicity.class);
                break;
            case R.id.identity_auth_layout:
                startActivity(DesignerIdentityAuthActivity.class);
                break;
            case R.id.product_auth_layout:
                startActivity(DesignerProductAuthActivity.class);
                break;
            case R.id.process_team_auth_layout:
                startActivity(DesignerTeamAuthActivity.class);
                break;
            case R.id.email_auth_layout:
//                startActivity();
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_designer_info_auth_list;
    }
}

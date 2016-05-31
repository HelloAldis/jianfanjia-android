package com.jianfanjia.cn.designer.ui.activity.my_info_auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.model.Designer;
import com.jianfanjia.api.request.designer.UpdateDesignerEmailInfoRequest;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.api.Api;
import com.jianfanjia.cn.designer.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.designer.business.DesignerBusiness;
import com.jianfanjia.cn.designer.config.Global;
import com.jianfanjia.cn.designer.ui.fragment.EmailAuthFinishedFragment;
import com.jianfanjia.cn.designer.ui.fragment.EmailAuthingFragment;
import com.jianfanjia.cn.designer.ui.fragment.EmailSendAuthFragment;
import com.jianfanjia.cn.designer.view.MainHeadView;
import com.jianfanjia.common.tool.LogTool;

/**
 * Description: com.jianfanjia.cn.designer.ui.activity.my_info_auth
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-05-18 14:03
 */
public class EmailAuthActivity extends BaseSwipeBackActivity implements EmailAuthingFragment.EmailAuthingCallback,
        EmailSendAuthFragment.SendAuthCallback,EmailAuthFinishedFragment.EmailFinishedCallback {

    public static final String EMAIL = "email";
    private static final String AUTH_FINISHED_FRAGMENT = "auth_finish_fragment";
    private static final String SEND_AUTH_FRAGMENT = "send_auth_fragment";
    private static final String AUTHING_FRAGMENT = "authing_fragment";

    @Bind(R.id.designer_auth_head_layout)
    MainHeadView mMainHeadView;

    private Designer mDesigner;
    private String email;

    private EmailAuthFinishedFragment mEmailAuthFinishedFragment;
    private EmailSendAuthFragment mEmailSendAuthFragment;
    private EmailAuthingFragment mEmailAuthingFragment;

    private String initAuthStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getDataFromIntent();
        initView();
        if (savedInstanceState == null) {
            initFragment();
        }
    }

    private void initFragment() {
        if (!TextUtils.isEmpty(initAuthStatus) && initAuthStatus.equals
                (DesignerBusiness.DESIGNER_NOT_AUTH)) {
            mEmailAuthingFragment = EmailAuthingFragment.getInstance(email);
            getSupportFragmentManager().beginTransaction().replace(R.id.container_layout, mEmailAuthingFragment,
                    AUTHING_FRAGMENT).addToBackStack(null).commit();
            getSupportFragmentManager().executePendingTransactions();
        } else if (!TextUtils.isEmpty(initAuthStatus) && initAuthStatus.equals
                (DesignerBusiness.DESIGNER_AUTH_SUCCESS)) {
            mEmailAuthFinishedFragment = EmailAuthFinishedFragment.getInstance(email);
            getSupportFragmentManager().beginTransaction().replace(R.id.container_layout, mEmailAuthFinishedFragment,
                    AUTH_FINISHED_FRAGMENT).addToBackStack(null).commit();
            getSupportFragmentManager().executePendingTransactions();
        } else {
            mEmailSendAuthFragment = EmailSendAuthFragment.getInstance(email);
            getSupportFragmentManager().beginTransaction().replace(R.id.container_layout, mEmailSendAuthFragment,
                    SEND_AUTH_FRAGMENT).addToBackStack(null).commit();
            getSupportFragmentManager().executePendingTransactions();
        }
    }


    @Override
    public void sendAgain() {

    }

    @Override
    public void updateEmil() {
        LogTool.d(this.getClass().getName(), "updateEmail");
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (mEmailSendAuthFragment == null) {
            mEmailSendAuthFragment = EmailSendAuthFragment.getInstance(email);
        } else {
            mEmailSendAuthFragment = (EmailSendAuthFragment) getSupportFragmentManager().findFragmentByTag(SEND_AUTH_FRAGMENT);
            mEmailSendAuthFragment.setEmail(email);
        }
        fragmentTransaction.replace(R.id.container_layout, mEmailSendAuthFragment,
                SEND_AUTH_FRAGMENT).addToBackStack(null).commit();
        getSupportFragmentManager().executePendingTransactions();
    }

    private void getDataFromIntent() {
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            mDesigner = (Designer) bundle.getSerializable(Global.DESIGNER_INFO);
        }
        email = mDesigner.getEmail();
        initAuthStatus = mDesigner.getEmail_auth_type();
    }

    private void initView() {
        initMainView();
    }

    private void initMainView() {
        mMainHeadView.setMianTitle(getString(R.string.email_auth));
    }

    @OnClick({R.id.head_back_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 返回上一个fragment
            if (getSupportFragmentManager().getBackStackEntryCount() > 1) {

            } else {
                appManager.finishActivity(this);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void sendEmailAuth(String email) {
        UpdateDesignerEmailInfoRequest updateDesignerEmailInfoRequest = new UpdateDesignerEmailInfoRequest();
        updateDesignerEmailInfoRequest.setEmail(email);

        Api.updateDesignerEmailInfo(updateDesignerEmailInfoRequest, new ApiCallback<ApiResponse<String>>() {
            @Override
            public void onPreLoad() {
                showWaitDialog();
            }

            @Override
            public void onHttpDone() {
                hideWaitDialog();
            }

            @Override
            public void onSuccess(ApiResponse<String> apiResponse) {
                dataManager.setDesigner(mDesigner);
                appManager.finishActivity(EmailAuthActivity.this);
            }

            @Override
            public void onFailed(ApiResponse<String> apiResponse) {
                makeTextShort(apiResponse.getErr_msg());
            }

            @Override
            public void onNetworkError(int code) {

            }
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_email_auth;
    }


    @Override
    public void sendEmailAuthFinish(String email) {
        LogTool.d(this.getClass().getName(), "updateEmail = " + email + " mEmailAuthingFragment =" + mEmailAuthingFragment);
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        this.email = email;
        if (mEmailAuthingFragment == null) {
            mEmailAuthingFragment = EmailAuthingFragment.getInstance(email);
        } else {
            mEmailAuthingFragment = (EmailAuthingFragment) getSupportFragmentManager().findFragmentByTag
                    (AUTHING_FRAGMENT);
        }
        fragmentTransaction.replace(R.id.container_layout, mEmailAuthingFragment,
                AUTHING_FRAGMENT).addToBackStack(null).commit();
        getSupportFragmentManager().executePendingTransactions();
        mEmailAuthingFragment.setEmail(email);
    }

    @Override
    public void updateEmail() {
        updateEmil();
    }
}

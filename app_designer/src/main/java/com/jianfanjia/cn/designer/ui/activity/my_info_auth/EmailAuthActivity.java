package com.jianfanjia.cn.designer.ui.activity.my_info_auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.model.Designer;
import com.jianfanjia.api.request.designer.UpdateDesignerEmailInfoRequest;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.api.Api;
import com.jianfanjia.cn.designer.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.designer.config.Global;
import com.jianfanjia.cn.designer.ui.activity.login_and_register.ForgetPswActivity;
import com.jianfanjia.cn.designer.view.MainHeadView;

/**
 * Description: com.jianfanjia.cn.designer.ui.activity.my_info_auth
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-05-18 14:03
 */
public class EmailAuthActivity extends BaseSwipeBackActivity {

    @Bind(R.id.act_input_email)
    EditText mEtLoginUserName = null;// 邮箱

    @Bind(R.id.btn_send)
    Button mBtnLogin = null;// 登录按钮

    @Bind(R.id.designer_auth_head_layout)
    MainHeadView mMainHeadView;

    private Designer mDesigner;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            mDesigner = (Designer) bundle.getSerializable(Global.DESIGNER_INFO);
        }
        if (mDesigner == null) {
            mDesigner = new Designer();
        }
    }

    private void initView() {
        initMainView();

    }

    private void initMainView() {
        mMainHeadView.setMianTitle(getString(R.string.email_auth));

    }

    @OnTextChanged(R.id.act_input_email)
    public void onAccountTextChanged(CharSequence text) {
        if (!TextUtils.isEmpty(text)) {
            mBtnLogin.setEnabled(true);
        } else {
            mBtnLogin.setEnabled(false);
        }
    }

    @OnClick({R.id.btn_send, R.id.head_back_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send:
                email = mEtLoginUserName.getText().toString().trim();
                if (checkLoginInput(email)) {
                    mDesigner.setEmail(email);
                    sendEmailAuth(mDesigner);
                }
                break;
            case R.id.act_forget_password:
                startActivity(ForgetPswActivity.class);
                break;
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
            default:
                break;
        }
    }

    private boolean checkLoginInput(String name) {
        if (!name.matches(Global.EMAIL_MATCH)) {
            makeTextShort(getString(R.string.tip_input_corrent_email));
            mEtLoginUserName.requestFocus();
            return false;
        }
        return true;
    }

    private void sendEmailAuth(final Designer designer){
        UpdateDesignerEmailInfoRequest updateDesignerEmailInfoRequest = new UpdateDesignerEmailInfoRequest();
        updateDesignerEmailInfoRequest.setDesigner(designer);

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
                dataManager.setDesigner(designer);
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
}

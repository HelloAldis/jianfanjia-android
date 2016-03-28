package com.jianfanjia.cn.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.request.guest.SendVerificationRequest;
import com.jianfanjia.cn.api.Api;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.RegisterInfo;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.UiHelper;

public class ForgetPswActivity extends BaseActivity {
    private static final String TAG = ForgetPswActivity.class.getClass()
            .getName();
    @Bind(R.id.act_forget_psw_input_phone)
    EditText mEtForgetPswUserName = null;// 注册用户名输入框
    @Bind(R.id.act_forget_psw_input_password)
    EditText mEtForgetPswPassword = null;// 注册用户密码输入框
    @Bind(R.id.btn_next)
    Button mBtnNext;
    @Bind(R.id.forget_psw_layout)
    RelativeLayout registerLayout;
    @Bind(R.id.act_forget_psw_input_password_delete)
    ImageView registerInputPasswordDelete;
    @Bind(R.id.act_forget_psw_input_phone_delete)
    ImageView registerInputPhoneDelete;

    private String mUserName = null;// 用户名
    private String mPassword = null;// 密码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initView();
    }

    @OnClick({R.id.head_back_layout,R.id.btn_next})
    void onClick(View view){
        int resId = view.getId();
        switch (resId){
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
            case R.id.btn_next:
                mUserName = mEtForgetPswUserName.getText().toString().trim();
                mPassword = mEtForgetPswPassword .getText().toString().trim();
                if(checkRegisterInput(mUserName,mPassword)){
                    sendVerification(mUserName,mPassword);
                }
                break;
        }
    }

    private boolean checkRegisterInput(String name, String password) {
        if (TextUtils.isEmpty(name)) {
            makeTextShort(getResources().getString(
                    R.string.tip_please_input_username));
            mEtForgetPswUserName.requestFocus();
            return false;
        }
        if(!name.matches(Global.PHONE_MATCH)){
            makeTextShort(getString(R.string.tip_input_corrent_phone));
            mEtForgetPswUserName.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            makeTextShort(getResources().getString(
                    R.string.tip_please_input_password));
            mEtForgetPswPassword.requestFocus();
            return false;
        }
        if(!password.matches(Global.PASSWORD_MATCH)){
            makeTextShort(getString(R.string.tip_input_correct_password));
            mEtForgetPswPassword.requestFocus();
            return false;
        }
        return true;
    }

    public void initView(){
        UiHelper.controlKeyboardLayout(registerLayout, mBtnNext);

        mBtnNext.setEnabled(false);

        mEtForgetPswUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                LogTool.d(TAG, "forgetPsw afterTextChanged");
                String text = s.toString();
                if(!TextUtils.isEmpty(text) && !TextUtils.isEmpty(mEtForgetPswPassword.getText().toString())){
                    mBtnNext.setEnabled(true);
                }else{
                    mBtnNext.setEnabled(false);
                }
            }
        });
        mEtForgetPswPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                LogTool.d(TAG,"forgetPsw afterTextChanged");
                String text = s.toString();
                if(!TextUtils.isEmpty(text) && !TextUtils.isEmpty(mEtForgetPswUserName.getText().toString())){
                    mBtnNext.setEnabled(true);
                }else{
                    mBtnNext.setEnabled(false);
                }
            }
        });
    }

    /**
     * 发送验证码
     * @param name
     * @param password
     */
    private void sendVerification(final String name, final String password) {
        SendVerificationRequest sendVerificationRequest = new SendVerificationRequest(name);

        Api.sendVerification(sendVerificationRequest, new ApiCallback<ApiResponse<String>>() {
            @Override
            public void onPreLoad() {

            }

            @Override
            public void onHttpDone() {

            }

            @Override
            public void onSuccess(ApiResponse<String> apiResponse) {
                RegisterInfo registerInfo = new RegisterInfo();
                registerInfo.setPass(password);
                registerInfo.setPhone(name);
                Bundle registerBundle = new Bundle();
                registerBundle.putSerializable(Global.REGISTER_INFO, registerInfo);
                registerBundle.putInt(Global.REGISTER, RegisterNewActivity.UPDATE_PSW_CODE);
                startActivity(RegisterNewActivity.class, registerBundle);
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
        return R.layout.activity_forget_psw;
    }
}
package com.jianfanjia.cn.designer.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.base.BaseAnnotationActivity;
import com.jianfanjia.cn.designer.bean.RegisterInfo;
import com.jianfanjia.cn.designer.config.Global;
import com.jianfanjia.cn.designer.http.JianFanJiaClient;
import com.jianfanjia.cn.designer.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.designer.tools.LogTool;
import com.jianfanjia.cn.designer.tools.UiHelper;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_forget_psw)
public class ForgetPswActivity extends BaseAnnotationActivity {
    private static final String TAG = ForgetPswActivity.class.getClass()
            .getName();
    @ViewById(R.id.act_forget_psw_input_phone)
    EditText mEtForgetPswUserName = null;// 注册用户名输入框
    @ViewById(R.id.act_forget_psw_input_password)
    EditText mEtForgetPswPassword = null;// 注册用户密码输入框
    @ViewById(R.id.btn_next)
    Button mBtnNext;
    @ViewById(R.id.forget_psw_layout)
    RelativeLayout registerLayout;
    @ViewById(R.id.act_forget_psw_input_password_delete)
    ImageView registerInputPasswordDelete;
    @ViewById(R.id.act_forget_psw_input_phone_delete)
    ImageView registerInputPhoneDelete;

    private String mUserName = null;// 用户名
    private String mPassword = null;// 密码

    @AfterViews
    protected void afterView() {
        mEtForgetPswUserName.requestFocus();
    }


    @Click({R.id.head_back_layout, R.id.btn_next})
    void onClick(View view) {
        int resId = view.getId();
        switch (resId) {
            case R.id.head_back_layout:
                finish();
                break;
            case R.id.btn_next:
                mUserName = mEtForgetPswUserName.getText().toString().trim();
                mPassword = mEtForgetPswPassword.getText().toString().trim();
                if (checkRegisterInput(mUserName, mPassword)) {
                    sendVerification(mUserName, mPassword);
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
        if (!name.matches(Global.PHONE_MATCH)) {
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
        if (!password.matches(Global.PASSWORD_MATCH)) {
            makeTextShort(getString(R.string.tip_input_correct_password));
            mEtForgetPswPassword.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
//        mEtForgetPswUserName.requestFocus();
    }

    @AfterViews
    void initUi() {
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
                if (!TextUtils.isEmpty(text) && !TextUtils.isEmpty(mEtForgetPswPassword.getText().toString())) {
                    mBtnNext.setEnabled(true);
                } else {
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
                LogTool.d(TAG, "forgetPsw afterTextChanged");
                String text = s.toString();
                if (!TextUtils.isEmpty(text) && !TextUtils.isEmpty(mEtForgetPswUserName.getText().toString())) {
                    mBtnNext.setEnabled(true);
                } else {
                    mBtnNext.setEnabled(false);
                }
            }
        });
    }

    /**
     * 发送验证码
     *
     * @param name
     * @param password
     */
    private void sendVerification(final String name, final String password) {
        JianFanJiaClient.send_verification(this, name, new ApiUiUpdateListener() {
            @Override
            public void preLoad() {

            }

            @Override
            public void loadSuccess(Object data) {
                RegisterInfo registerInfo = new RegisterInfo();
                registerInfo.setPass(password);
                registerInfo.setPhone(name);
                Bundle registerBundle = new Bundle();
                registerBundle.putSerializable(Global.REGISTER_INFO, registerInfo);
                registerBundle.putInt(Global.REGISTER, RegisterNewActivity.UPDATE_PSW_CODE);
                startActivity(RegisterNewActivity_.class, registerBundle);
            }

            @Override
            public void loadFailture(String error_msg) {
                makeTextLong(error_msg);
            }
        }, this);
    }


}
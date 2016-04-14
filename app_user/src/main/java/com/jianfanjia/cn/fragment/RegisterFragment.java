package com.jianfanjia.cn.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.HttpCode;
import com.jianfanjia.api.request.guest.SendVerificationRequest;
import com.jianfanjia.api.request.guest.VerifyPhoneRequest;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.RegisterNewActivity;
import com.jianfanjia.cn.api.Api;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.bean.RegisterInfo;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.constant.IntentConstant;
import com.jianfanjia.common.tool.LogTool;

/**
 * Description: com.jianfanjia.cn.fragment
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-04-05 11:22
 */
public class RegisterFragment extends BaseFragment{

    private static final String TAG = RegisterFragment.class.getClass().getName();

    @Bind(R.id.act_forget_psw_input_phone)
    EditText mEtRegisterUserName = null;// 注册用户名输入框
    @Bind(R.id.act_forget_psw_input_password)
    EditText mEtRegisterPassword = null;// 注册用户密码输入框
    @Bind(R.id.btn_next)
    Button mBtnNext = null;// 下一步

    private String mUserName = null;// 用户名
    private String mPassword = null;// 密码

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        initView();
        return view;
    }

    private void initView(){
        mEtRegisterUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                LogTool.d(TAG, "registerlogin afterTextChanged");
                String text = s.toString();
                if (!TextUtils.isEmpty(text) && !TextUtils.isEmpty(mEtRegisterPassword.getText().toString())) {
                    mBtnNext.setEnabled(true);
                } else {
                    mBtnNext.setEnabled(false);
//                    verifyPhone(text);
                }
            }
        });
        mEtRegisterPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                LogTool.d(TAG, "registerpassword afterTextChanged");
                String text = s.toString();
                if (!TextUtils.isEmpty(text) && !TextUtils.isEmpty(mEtRegisterUserName.getText().toString())) {
                    mBtnNext.setEnabled(true);
                } else {
                    mBtnNext.setEnabled(false);
                }
            }
        });
    }

    @OnClick({R.id.btn_next})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_next:
                mUserName = mEtRegisterUserName.getText().toString().trim();
                mPassword = mEtRegisterPassword.getText().toString().trim();
                if (checkRegisterInput(mUserName, mPassword)) {
                    verifyPhone(mUserName);
                }
                break;
            default:
                break;
        }
    }

    private boolean checkRegisterInput(String name, String password) {
        if (TextUtils.isEmpty(name)) {
            makeTextShort(getResources().getString(
                    R.string.tip_please_input_username));
            mEtRegisterUserName.requestFocus();
            return false;
        }
        if (!name.matches(Global.PHONE_MATCH)) {
            makeTextShort(getString(R.string.tip_input_corrent_phone));
            mEtRegisterUserName.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            makeTextShort(getResources().getString(
                    R.string.tip_please_input_password));
            mEtRegisterPassword.requestFocus();
            return false;
        }
        if (!password.matches(Global.PASSWORD_MATCH)) {
            makeTextShort(getString(R.string.tip_input_correct_password));
            mEtRegisterPassword.requestFocus();
            return false;
        }
        return true;
    }

    private void verifyPhone(final String phone) {
        VerifyPhoneRequest verifyPhoneRequest = new VerifyPhoneRequest();
        verifyPhoneRequest.setPhone(phone);
        Api.verifyPhone(verifyPhoneRequest, new ApiCallback<ApiResponse<String>>() {
            @Override
            public void onPreLoad() {

            }

            @Override
            public void onHttpDone() {

            }

            @Override
            public void onSuccess(ApiResponse<String> apiResponse) {
                sendVerification(mUserName, mPassword);
            }

            @Override
            public void onFailed(ApiResponse<String> apiResponse) {
                makeTextShort(apiResponse.getErr_msg());
            }

            @Override
            public void onNetworkError(int code) {
                makeTextShort(HttpCode.NO_NETWORK_ERROR_MSG);
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
        SendVerificationRequest sendVerificationRequest = new SendVerificationRequest();
        sendVerificationRequest.setPhone(name);
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
                registerBundle.putSerializable(IntentConstant.REGISTER_INFO, registerInfo);
                registerBundle.putInt(IntentConstant.REGISTER, RegisterNewActivity.REGISTER_CODE);
                startActivity(RegisterNewActivity.class, registerBundle);
            }

            @Override
            public void onFailed(ApiResponse<String> apiResponse) {
                makeTextShort(apiResponse.getErr_msg());
            }

            @Override
            public void onNetworkError(int code) {
                makeTextShort(HttpCode.NO_NETWORK_ERROR_MSG);
            }
        });
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_register;
    }
}

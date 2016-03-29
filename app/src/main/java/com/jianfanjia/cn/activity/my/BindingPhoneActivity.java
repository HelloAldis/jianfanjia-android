package com.jianfanjia.cn.activity.my;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.request.guest.SendVerificationRequest;
import com.jianfanjia.api.request.guest.VerifyPhoneRequest;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.RegisterNewActivity;
import com.jianfanjia.cn.activity.SwipeBackActivity;
import com.jianfanjia.cn.api.Api;
import com.jianfanjia.cn.bean.RegisterInfo;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.common.tool.LogTool;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author zhanghao
 * @ClassName: BindingPhoneActivity
 * @Description: 绑定手机号
 * @date 2015-10-27 下午12:11:23
 */
public class BindingPhoneActivity extends SwipeBackActivity {
    private static final String TAG = BindingPhoneActivity.class.getName();
    @Bind(R.id.act_binding_input_phone)
    EditText mEtPhone;// 用户名输入框
    @Bind(R.id.btn_commit)
    Button mBtnCommit;

    private String phone = null;// 密码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        mBtnCommit.setEnabled(false);
        mEtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                LogTool.d(TAG, "register afterTextChanged");
                String text = s.toString();
                if (!TextUtils.isEmpty(text)) {
                    mBtnCommit.setEnabled(true);
                } else {
                    mBtnCommit.setEnabled(false);
                }
            }
        });
    }

    @OnClick({R.id.head_back_layout, R.id.btn_commit})
    void OnClick(View view) {
        switch (view.getId()) {
            case R.id.btn_commit:
                phone = mEtPhone.getText().toString().trim();
                if (checkInput(phone)) {
                    verifyPhone(phone);
                }
                break;
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
            default:
                break;
        }

    }

    private boolean checkInput(String phone) {
        if (TextUtils.isEmpty(phone)) {
            makeTextShort(getResources().getString(
                    R.string.tip_please_input_username));
            mEtPhone.requestFocus();
            return false;
        }
        if (!phone.matches(Global.PHONE_MATCH)) {
            makeTextShort(getString(R.string.tip_input_corrent_phone));
            mEtPhone.requestFocus();
            return false;
        }
        return true;
    }

    private void verifyPhone(final String phone) {
        VerifyPhoneRequest request = new VerifyPhoneRequest(phone);
        Api.verifyPhone(request, new ApiCallback<ApiResponse<String>>() {
            @Override
            public void onPreLoad() {

            }

            @Override
            public void onHttpDone() {

            }

            @Override
            public void onSuccess(ApiResponse<String> apiResponse) {
                sendVerification(phone);
            }

            @Override
            public void onFailed(ApiResponse<String> apiResponse) {

            }

            @Override
            public void onNetworkError(int code) {

            }
        });
    }

    /**
     * 发送验证码
     *
     * @param name
     */
    private void sendVerification(final String name) {
        SendVerificationRequest request = new SendVerificationRequest(name);
        Api.sendVerification(request, new ApiCallback<ApiResponse<String>>() {
            @Override
            public void onPreLoad() {

            }

            @Override
            public void onHttpDone() {

            }

            @Override
            public void onSuccess(ApiResponse<String> apiResponse) {
                RegisterInfo registerInfo = new RegisterInfo();
                registerInfo.setPhone(name);
                Bundle registerBundle = new Bundle();
                registerBundle.putSerializable(Global.REGISTER_INFO, registerInfo);
                registerBundle.putInt(Global.REGISTER, RegisterNewActivity.BINDING_PHONE);
                registerBundle.putInt(Global.BINDING_PHONE_INTENT, getIntent().getExtras().getInt(Global
                        .BINDING_PHONE_INTENT));
                startActivity(RegisterNewActivity.class, registerBundle);
                appManager.finishActivity(BindingPhoneActivity.this);
            }

            @Override
            public void onFailed(ApiResponse<String> apiResponse) {

            }

            @Override
            public void onNetworkError(int code) {

            }
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_binding_phone;
    }
}

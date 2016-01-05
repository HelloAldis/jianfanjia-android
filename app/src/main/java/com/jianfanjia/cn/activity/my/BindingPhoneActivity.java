package com.jianfanjia.cn.activity.my;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.RegisterNewActivity;
import com.jianfanjia.cn.activity.RegisterNewActivity_;
import com.jianfanjia.cn.base.BaseAnnotationActivity;
import com.jianfanjia.cn.bean.RegisterInfo;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.tools.LogTool;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * @author zhanghao
 * @ClassName: BindingPhoneActivity
 * @Description: 绑定手机号
 * @date 2015-10-27 下午12:11:23
 */
@EActivity(R.layout.activity_binding_phone)
public class BindingPhoneActivity extends BaseAnnotationActivity implements
        ApiUiUpdateListener {
    private static final String TAG = BindingPhoneActivity.class.getName();
    @ViewById(R.id.act_binding_input_phone)
    EditText mEtPhone;// 用户名输入框
    @ViewById(R.id.btn_commit)
    Button mBtnCommit;

    private String phone = null;// 密码

    @AfterViews
    public void initAnnotationView() {
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

    @Click({R.id.head_back_layout, R.id.btn_commit})
    void OnClick(View view) {
        switch (view.getId()) {
            case R.id.btn_commit:
                phone = mEtPhone.getText().toString().trim();
                if (checkInput(phone)) {
                    JianFanJiaClient.verifyPhone(this, phone, this, this);
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

    @Override
    public void loadSuccess(Object data) {
        //登录成功，加载首页
        super.loadSuccess(data);
        sendVerification(phone);
    }

    /**
     * 发送验证码
     *
     * @param name
     *
     */
    private void sendVerification(final String name) {
        JianFanJiaClient.send_verification(this, name, new ApiUiUpdateListener() {
            @Override
            public void preLoad() {

            }

            @Override
            public void loadSuccess(Object data) {
                RegisterInfo registerInfo = new RegisterInfo();
                registerInfo.setPhone(name);
                Bundle registerBundle = new Bundle();
                registerBundle.putSerializable(Global.REGISTER_INFO, registerInfo);
                registerBundle.putInt(Global.REGISTER, RegisterNewActivity.BINDING_PHONE);
                startActivity(RegisterNewActivity_.class, registerBundle);
                appManager.finishActivity(BindingPhoneActivity.this);
            }

            @Override
            public void loadFailture(String error_msg) {
                makeTextLong(error_msg);
            }
        }, this);
    }
}

package com.jianfanjia.cn.designer.activity.my;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.activity.RegisterNewActivity;
import com.jianfanjia.cn.designer.base.BaseActivity;
import com.jianfanjia.cn.designer.bean.RegisterInfo;
import com.jianfanjia.cn.designer.config.Global;
import com.jianfanjia.cn.designer.http.JianFanJiaClient;
import com.jianfanjia.cn.designer.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.designer.tools.LogTool;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author zhanghao
 * @ClassName: BindingPhoneActivity
 * @Description: 绑定手机号
 * @date 2015-10-27 下午12:11:23
 */
public class BindingPhoneActivity extends BaseActivity implements
        ApiUiUpdateListener {
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
    public void onClick(View view) {
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
                startActivity(RegisterNewActivity.class, registerBundle);
                appManager.finishActivity(BindingPhoneActivity.this);
            }

            @Override
            public void loadFailture(String error_msg) {
                makeTextLong(error_msg);
            }
        }, this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_binding_phone;
    }
}

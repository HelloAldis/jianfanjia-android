package com.jianfanjia.cn.designer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.cn.designer.Event.BindingPhoneEvent;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.base.BaseActivity;
import com.jianfanjia.cn.designer.bean.RegisterInfo;
import com.jianfanjia.cn.designer.config.Global;
import com.jianfanjia.cn.designer.http.JianFanJiaClient;
import com.jianfanjia.cn.designer.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.designer.tools.GeTuiManager;
import com.jianfanjia.cn.designer.tools.LogTool;
import de.greenrobot.event.EventBus;

/**
 * @author zhanghao
 * @ClassName: RegisterNewActivity
 * @Description: 注册
 * @date 2015-10-27 下午12:11:23
 */
public class RegisterNewActivity extends BaseActivity implements
        ApiUiUpdateListener {
    private static final String TAG = RegisterNewActivity.class.getName();

    public static final int REGISTER_CODE = 0;
    public static final int UPDATE_PSW_CODE = 1;
    public static final int BINDING_PHONE = 2;

    @Bind(R.id.et_verification)
    EditText mEtVerification;// 用户名输入框
    @Bind(R.id.register_phone)
    TextView mPhoneView;//手机号码
    @Bind(R.id.btn_commit)
    Button mBtnCommit;

    private String mVerification = null;// 密码
    private RegisterInfo registerInfo = null;

    int requsetCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getDataFromIntent();
        initView();
    }

    private void getDataFromIntent(){
        Intent intent = getIntent();
        registerInfo = (RegisterInfo) intent.getSerializableExtra(Global.REGISTER_INFO);
        requsetCode = intent.getIntExtra(Global.REGISTER, 0);
    }

    public void initView() {

        if (registerInfo != null) {
            mPhoneView.setText(registerInfo.getPhone());
        }
        mBtnCommit.setEnabled(false);

        mEtVerification.addTextChangedListener(new TextWatcher() {
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
                mVerification = mEtVerification.getText().toString().trim();
                if (checkInput(mVerification)) {
                    registerInfo.setCode(mVerification);
                    register(registerInfo);
                }
                break;
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
            default:
                break;
        }

    }

    /**
     * 注册
     *
     * @param registerInfo
     */
    private void register(RegisterInfo registerInfo) {
        switch (requsetCode) {
            case REGISTER_CODE:
                JianFanJiaClient.register(this, registerInfo, this, this);
                break;
            case UPDATE_PSW_CODE:
                JianFanJiaClient.update_psw(this, registerInfo, this, this);
                break;
            case BINDING_PHONE:
                JianFanJiaClient.bindingPhone(this, registerInfo, this, this);
                break;
        }
    }

    private boolean checkInput(String verification) {
        if (TextUtils.isEmpty(verification)) {
            makeTextShort(getResources().getString(
                    R.string.hint_verification));
            mEtVerification.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public void loadSuccess(Object data) {
        //登录成功，加载首页
        super.loadSuccess(data);
        switch (requsetCode) {
            case REGISTER_CODE:
                startActivity(MainActivity.class);
                appManager.finishActivity(this);
                GeTuiManager.bindGeTui(getApplicationContext(), dataManager.getUserId());
                break;
            case UPDATE_PSW_CODE:
                makeTextShort(getString(R.string.update_psw_success));
                startActivity(LoginNewActivity.class);
                appManager.finishActivity(this);
                break;
            case BINDING_PHONE:
                EventBus.getDefault().post(new BindingPhoneEvent(registerInfo.getPhone()));
                appManager.finishActivity(this);
                break;
        }
    }

    @Override
    public void loadFailture(String error_msg) {
        super.loadFailture(error_msg);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_register_new;
    }
}

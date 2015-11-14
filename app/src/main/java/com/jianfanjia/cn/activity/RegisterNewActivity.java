package com.jianfanjia.cn.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jianfanjia.cn.base.BaseAnnotationActivity;
import com.jianfanjia.cn.bean.RegisterInfo;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.tools.NetTool;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * @author zhanghao
 * @ClassName: RegisterNewActivity
 * @Description: 注册
 * @date 2015-10-27 下午12:11:23
 */
@EActivity(R.layout.activity_register_new)
public class RegisterNewActivity extends BaseAnnotationActivity implements
        ApiUiUpdateListener {
    private static final String TAG = RegisterNewActivity.class.getName();

    public static final int REGISTER_CODE = 0;
    public static final int UPDATE_PSW_CODE = 1;

    @ViewById(R.id.forget_psw_layout)
    RelativeLayout registerLayout;
    @ViewById(R.id.success_layout)
    LinearLayout successLayout;

    @ViewById(R.id.et_verification)
    EditText mEtVerification;// 用户名输入框
    @ViewById(R.id.btn_scan)
    Button mBtnScan;//浏览
    @ViewById(R.id.btn_publish_requirement)
    Button mBtnPublishRequirement;
    @ViewById(R.id.register_phone)
    TextView mPhoneView;//手机号码

    private String mVerification = null;// 密码
    private RegisterInfo registerInfo = null;

    int requsetCode;

    @AfterViews
    public void initView() {
        Intent intent = getIntent();
        registerInfo = (RegisterInfo) intent.getSerializableExtra(Global.REGISTER_INFO);
        requsetCode = intent.getIntExtra(Global.REGISTER,0);
        if (registerInfo != null) {
            mPhoneView.setText(registerInfo.getPhone());
        }
        registerLayout.setVisibility(View.VISIBLE);
        successLayout.setVisibility(View.GONE);
    }

    @Click({R.id.head_back_layout, R.id.btn_scan, R.id.btn_publish_requirement, R.id.btn_commit})
    void OnClick(View view) {
        switch (view.getId()) {
            case R.id.btn_commit:
                mVerification = mEtVerification.getText().toString().trim();
                if (checkInput(mVerification)) {
                    registerInfo.setCode(mVerification);
                    register(registerInfo);
                }
                break;
            case R.id.btn_scan:
                startActivity(MainActivity.class);
                finish();
                break;
            case R.id.btn_publish_requirement:
                Bundle bundle = new Bundle();
                bundle.putBoolean(Global.IS_PUBLISHREQUIREMENT,true);
                startActivity(MainActivity.class,bundle);
                finish();
                break;
            case R.id.head_back_layout:
                finish();
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
        if (NetTool.isNetworkAvailable(this)) {
            if(requsetCode == REGISTER_CODE){
                JianFanJiaClient.register(this, registerInfo, this, this);
            }else{
                JianFanJiaClient.update_psw(this,registerInfo,this,this);
            }
        } else {
            makeTextLong(getResources().getString(R.string.tip_internet_not));
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
        if(requsetCode == REGISTER_CODE){
            registerLayout.setVisibility(View.GONE);
            successLayout.setVisibility(View.VISIBLE);
        }else{
            startActivity(LoginNewActivity_.class);
            finish();
        }
    }

    @Override
    public void loadFailture(String error_msg) {
        super.loadFailture(error_msg);
    }
}

package com.jianfanjia.cn.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;
import com.jianfanjia.cn.base.BaseActivity;

/**
 * Description:登录
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class LoginActivity extends BaseActivity implements OnClickListener {
    private static final String TAG = LoginActivity.class.getName();
    private RelativeLayout loginLayout = null;
    private EditText mEtUserName = null;// 用户名输入框
    private EditText mEtPassword = null;// 用户密码输入框
    private Button mBtnLogin = null;// 登录按钮
    private TextView mForgetPswView = null;
    private TextView mRegisterView = null;// 导航到用户注册
    private String mUserName = null;// 用户名
    private String mPassword = null;// 密码

    @Override
    public void initView() {
        loginLayout = (RelativeLayout) findViewById(R.id.loginLayout);
        mEtUserName = (EditText) findViewById(R.id.et_username);
        mEtPassword = (EditText) findViewById(R.id.et_password);
        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mForgetPswView = (TextView) findViewById(R.id.forget_password);
        mRegisterView = (TextView) findViewById(R.id.register);
    }

    @Override
    public void setListener() {
        mBtnLogin.setOnClickListener(this);
        mRegisterView.setOnClickListener(this);
        mForgetPswView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                mUserName = mEtUserName.getText().toString().trim();
                mPassword = mEtPassword.getText().toString().trim();

                break;
            case R.id.register:
                startActivity(RegisterActivity.class);
                break;
            case R.id.forget_password:
                startActivity(ForgetPswActivity.class);
                break;
            default:
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }
}
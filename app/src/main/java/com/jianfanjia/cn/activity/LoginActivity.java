package com.jianfanjia.cn.activity;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.base.BaseResponse;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.LoadDataListener;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.NetTool;

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
        mUserName = dataManager.getAccount();
        mPassword = dataManager.getPassword();
        LogTool.d(TAG, "mUserName=" + mUserName + " mPassword=" + mPassword);
        if (!TextUtils.isEmpty(mUserName)) {
            mEtUserName.setText(mUserName);
        }
        if (!TextUtils.isEmpty(mPassword)) {
            mEtPassword.setText(mPassword);
        }
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
                if (checkInput(mUserName, mPassword)) {
                    login(mUserName, mPassword);
                }
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

    private boolean checkInput(String name, String password) {
        if (TextUtils.isEmpty(name)) {
            makeTextShort(getResources().getString(
                    R.string.tip_please_input_username));
            mEtUserName.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            makeTextShort(getResources().getString(
                    R.string.tip_please_input_password));
            mEtPassword.requestFocus();
            return false;
        }
        return true;
    }

    /**
     * 登录
     *
     * @param name
     * @param password
     */
    private void login(String name, String password) {
        if (NetTool.isNetworkAvailable(this)) {
            JianFanJiaClient.login(this, name, password, this, this);
        } else {
            makeTextLong(getString(R.string.tip_internet_not));
        }
    }

    @Override
    public void loadSuccess(BaseResponse baseResponse) {
        //登录成功，加载工地列表
        JianFanJiaClient.get_Process_List(this, new LoadDataListener() {

            @Override
            public void preLoad() {
                // TODO Auto-generated method stub

            }

            @Override
            public void loadSuccess(BaseResponse baseResponse) {
                // 工地列表刷新成功，加载用户默认工地
                String processId = dataManager.getDefaultProcessId();
                if (processId != null) {
                    JianFanJiaClient.get_ProcessInfo_By_Id(LoginActivity.this, processId, new LoadDataListener() {

                        @Override
                        public void preLoad() {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void loadSuccess(BaseResponse baseResponse) {
                            hideWaitDialog();
                            startActivity(MainActivity.class);
                            finish();
                        }

                        @Override
                        public void loadFailture() {
                            hideWaitDialog();
                            makeTextLong(getString(R.string.tip_error_internet));
                        }
                    }, this);
                } else {
                    hideWaitDialog();
                    startActivity(MainActivity.class);
                    finish();
                }

            }

            @Override
            public void loadFailture() {
                // TODO Auto-generated method stub
                hideWaitDialog();
                makeTextLong(getString(R.string.tip_error_internet));
            }
        }, this);

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }
}
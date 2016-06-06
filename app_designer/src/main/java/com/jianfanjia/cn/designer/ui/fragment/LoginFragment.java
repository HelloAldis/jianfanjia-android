package com.jianfanjia.cn.designer.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.HttpCode;
import com.jianfanjia.api.model.Designer;
import com.jianfanjia.api.request.designer.GetDesignerInfoRequest;
import com.jianfanjia.api.request.guest.LoginRequest;
import com.jianfanjia.cn.designer.AppManager;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.api.Api;
import com.jianfanjia.cn.designer.base.BaseFragment;
import com.jianfanjia.cn.designer.business.DataManagerNew;
import com.jianfanjia.cn.designer.business.DesignerBusiness;
import com.jianfanjia.cn.designer.config.Constant;
import com.jianfanjia.cn.designer.config.Global;
import com.jianfanjia.cn.designer.tools.AuthUtil;
import com.jianfanjia.cn.designer.ui.activity.MainActivity;
import com.jianfanjia.cn.designer.ui.activity.login_and_register.DesignerAgreementActivity;
import com.jianfanjia.cn.designer.ui.activity.login_and_register.ForgetPswActivity;

/**
 * Description: com.jianfanjia.cn.fragment
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-04-05 11:21
 */
public class LoginFragment extends BaseFragment {

    @Bind(R.id.act_login_input_phone)
    EditText mEtLoginUserName = null;// 用户名输入框
    @Bind(R.id.act_login_input_password)
    EditText mEtLoginPassword = null;// 用户密码输入框
    @Bind(R.id.btn_login)
    Button mBtnLogin = null;// 登录按钮

    private String mUserName = null;// 用户名
    private String mPassword = null;// 密码

    private AuthUtil authUtil = null;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        authUtil = AuthUtil.getInstance(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @OnTextChanged(R.id.act_login_input_phone)
    public void onAccountTextChanged(CharSequence text) {
        if (!TextUtils.isEmpty(text)) {
            mBtnLogin.setEnabled(true);
        } else {
            mBtnLogin.setEnabled(false);
        }
    }

    @OnTextChanged(R.id.act_login_input_password)
    public void onPswTextChanged(CharSequence text) {
        if (!TextUtils.isEmpty(text)) {
            mBtnLogin.setEnabled(true);
        } else {
            mBtnLogin.setEnabled(false);
        }
    }

    @OnClick({R.id.btn_login, R.id.act_forget_password, R.id
            .btn_login_weixin_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                mUserName = mEtLoginUserName.getText().toString().trim();
                mPassword = mEtLoginPassword.getText().toString().trim();
                if (checkLoginInput(mUserName, mPassword)) {
                    login(mUserName, mPassword);
                }
                break;
            case R.id.act_forget_password:
                startActivity(ForgetPswActivity.class);
                break;
            default:
                break;
        }
    }

    private boolean checkLoginInput(String name, String password) {
        if (TextUtils.isEmpty(name)) {
            makeTextShort(getResources().getString(
                    R.string.tip_please_input_username));
            mEtLoginUserName.requestFocus();
            return false;
        }
        if (!name.matches(Global.PHONE_MATCH)) {
            makeTextShort(getString(R.string.tip_input_corrent_phone));
            mEtLoginUserName.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            makeTextShort(getResources().getString(
                    R.string.tip_please_input_password));
            mEtLoginPassword.requestFocus();
            return false;
        }
        if (!password.matches(Global.PASSWORD_MATCH)) {
            makeTextShort(getString(R.string.tip_input_correct_password));
            mEtLoginPassword.requestFocus();
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
    private void login(String name, final String password) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPhone(name);
        loginRequest.setPass(password);
        Api.login(loginRequest, new ApiCallback<ApiResponse<Designer>>() {
            @Override
            public void onPreLoad() {
                showWaitDialog();
            }

            @Override
            public void onHttpDone() {
            }

            @Override
            public void onSuccess(ApiResponse<Designer> apiResponse) {
                getDesignerInfo();
            }

            @Override
            public void onFailed(ApiResponse<Designer> apiResponse) {
                hideWaitDialog();
                makeTextShort(apiResponse.getErr_msg());
            }

            @Override
            public void onNetworkError(int code) {
                hideWaitDialog();
                makeTextShort(HttpCode.NO_NETWORK_ERROR_MSG);
            }
        });
    }

    private void getDesignerInfo() {
        GetDesignerInfoRequest getDesignerInfoRequest = new GetDesignerInfoRequest();

        Api.getDesignerInfo(getDesignerInfoRequest, new ApiCallback<ApiResponse<Designer>>() {
            @Override
            public void onPreLoad() {

            }

            @Override
            public void onHttpDone() {
                hideWaitDialog();
            }

            @Override
            public void onSuccess(ApiResponse<Designer> apiResponse) {
                Designer designer = apiResponse.getData();
                DataManagerNew.loginSuccess(designer);
                if (!DesignerBusiness.AGREE_LICENSE.equals(designer.getAgreee_license())) {
                    startActivity(DesignerAgreementActivity.class);
                } else {
                    dataManager.setLogin(true);
                    if (!DesignerBusiness.isFinishBaseAuth(designer)) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(MainActivity.TAB_POSITION, Constant.MORE);
                        startActivity(MainActivity.class, bundle);
                    } else {
                        startActivity(MainActivity.class);
                    }
                }
                AppManager.getAppManager().finishActivity(getActivity());
            }

            @Override
            public void onFailed(ApiResponse<Designer> apiResponse) {
                makeTextShort(getString(R.string.login_fail));
            }

            @Override
            public void onNetworkError(int code) {
                makeTextShort(HttpCode.NO_NETWORK_ERROR_MSG);
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_login;
    }
}

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

import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.HttpCode;
import com.jianfanjia.api.model.User;
import com.jianfanjia.api.request.guest.LoginRequest;
import com.jianfanjia.api.request.guest.WeiXinRegisterRequest;
import com.jianfanjia.cn.AppManager;
import com.jianfanjia.cn.activity.ForgetPswActivity;
import com.jianfanjia.cn.activity.LoginNewActivity;
import com.jianfanjia.cn.activity.MainActivity;
import com.jianfanjia.cn.activity.NewUserCollectDecStageActivity;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.api.Api;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.business.DataManagerNew;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.tools.AuthUtil;
import com.jianfanjia.common.tool.LogTool;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.listener.SocializeListeners;

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

    private AuthUtil authUtil;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        authUtil = AuthUtil.getInstance(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        initView();
        return view;
    }

    private void initView() {
        mEtLoginUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                if (!TextUtils.isEmpty(text) && !TextUtils.isEmpty(mEtLoginPassword.getText().toString())) {
                    mBtnLogin.setEnabled(true);
                } else {
                    mBtnLogin.setEnabled(false);
//                    verifyPhone(text);
                }
            }
        });
        mEtLoginPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                if (!TextUtils.isEmpty(text) && !TextUtils.isEmpty(mEtLoginUserName.getText().toString())) {
                    mBtnLogin.setEnabled(true);
                } else {
                    mBtnLogin.setEnabled(false);
//                    verifyPhone(text);
                }
            }
        });
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
            case R.id.btn_login_weixin_layout:
                SHARE_MEDIA platform = SHARE_MEDIA.WEIXIN;
                authUtil.doOauthVerify((LoginNewActivity) getActivity(), platform, umDataListener);
                break;
            default:
                break;
        }
    }

    private SocializeListeners.UMDataListener umDataListener = new SocializeListeners.UMDataListener() {
        @Override
        public void onStart() {

        }

        @Override
        public void onComplete(int i, Map<String, Object> data) {
            if (i == 200 && data != null) {
                LogTool.d(this.getClass().getName(), data.toString());
                WeiXinRegisterRequest weiXinRegisterRequest = new WeiXinRegisterRequest();
                weiXinRegisterRequest.setUsername(data.get("nickname").toString());
                weiXinRegisterRequest.setImage_url((String) data.get("headimgurl").toString());
                String sex = null;
                if ((sex = data.get("sex").toString()) != null) {
                    weiXinRegisterRequest.setSex(sex.equals(Constant.SEX_MAN) ? Constant.SEX_WOMEN : Constant.SEX_MAN);
                    //系统的性别和微信的性别要转换
                }
                weiXinRegisterRequest.setWechat_openid(data.get("openid").toString());
                weiXinRegisterRequest.setWechat_unionid(data.get("unionid").toString());

                Api.weiXinRegister(weiXinRegisterRequest, new ApiCallback<ApiResponse<User>>() {
                    @Override
                    public void onPreLoad() {

                    }

                    @Override
                    public void onHttpDone() {

                    }

                    @Override
                    public void onSuccess(ApiResponse<User> apiResponse) {

                        User loginUserBean = apiResponse.getData();
                        loginUserBean.setWechat_openid(loginUserBean.getWechat_openid());
                        loginUserBean.setWechat_unionid(loginUserBean.getWechat_unionid());
                        DataManagerNew.loginSuccess(loginUserBean);

                        if (dataManager.getWeixinFisrtLogin()) {
                            startActivity(NewUserCollectDecStageActivity.class);
                        } else {
                            startActivity(MainActivity.class);
                        }
                        AppManager.getAppManager().finishActivity(getActivity());
                    }

                    @Override
                    public void onFailed(ApiResponse<User> apiResponse) {

                    }

                    @Override
                    public void onNetworkError(int code) {

                    }
                });
            } else {
                hideWaitDialog();
                makeTextShort(getString(R.string.authorize_fail));
            }
        }
    };

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
        Api.login(loginRequest, new ApiCallback<ApiResponse<User>>() {
            @Override
            public void onPreLoad() {
                showWaitDialog();
            }

            @Override
            public void onHttpDone() {
                hideWaitDialog();
            }

            @Override
            public void onSuccess(ApiResponse<User> apiResponse) {
                User loginUserBean = apiResponse.getData();
                loginUserBean.setPass(password);
                DataManagerNew.loginSuccess(loginUserBean);

                startActivity(MainActivity.class);
                AppManager.getAppManager().finishActivity(getActivity());
            }

            @Override
            public void onFailed(ApiResponse<User> apiResponse) {
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
        return R.layout.fragment_login;
    }
}

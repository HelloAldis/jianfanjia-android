package com.jianfanjia.cn.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import com.aldis.hud.Hud;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.HttpCode;
import com.jianfanjia.api.model.User;
import com.jianfanjia.api.request.guest.LoginRequest;
import com.jianfanjia.api.request.guest.WeiXinRegisterRequest;
import com.jianfanjia.cn.AppManager;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.api.Api;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.business.DataManagerNew;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.tools.AuthUtil;
import com.jianfanjia.cn.ui.activity.MainActivity;
import com.jianfanjia.cn.ui.activity.loginandreg.ForgetPswActivity;
import com.jianfanjia.cn.ui.activity.loginandreg.LoginNewActivity;
import com.jianfanjia.cn.ui.activity.welcome.NewUserCollectDecStageActivity;
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
                LogTool.d(data.toString());
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
                        makeTextShort(apiResponse.getErr_msg());
                    }

                    @Override
                    public void onNetworkError(int code) {
                        makeTextShort(HttpCode.getMsg(code));
                    }
                },LoginFragment.this);
            } else {
                Hud.dismiss();
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
                Hud.show(getUiContext());
            }

            @Override
            public void onHttpDone() {
                Hud.dismiss();
            }

            @Override
            public void onSuccess(ApiResponse<User> apiResponse) {
                User loginUserBean = apiResponse.getData();
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
                makeTextShort(HttpCode.getMsg(code));
            }
        },this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_login;
    }
}

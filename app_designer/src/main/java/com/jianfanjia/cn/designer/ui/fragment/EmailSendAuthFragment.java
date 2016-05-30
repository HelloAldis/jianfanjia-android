package com.jianfanjia.cn.designer.ui.fragment;

import android.app.Activity;
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
import com.jianfanjia.api.request.designer.SendVerifyEmailRequest;
import com.jianfanjia.api.request.designer.UpdateDesignerEmailInfoRequest;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.api.Api;
import com.jianfanjia.cn.designer.base.BaseFragment;
import com.jianfanjia.cn.designer.config.Global;
import com.jianfanjia.cn.designer.ui.activity.my_info_auth.EmailAuthActivity;

/**
 * Description: com.jianfanjia.cn.designer.ui.fragment
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-05-30 15:59
 */
public class EmailSendAuthFragment extends BaseFragment {

    @Bind(R.id.act_input_email)
    EditText mEtLoginUserName = null;// 邮箱

    @Bind(R.id.btn_send)
    Button mBtnLogin = null;// 登录按钮

    private String mEmail;

    private SendAuthCallback mSendAuthCallback;

    public static EmailSendAuthFragment getInstance(String email){
        EmailSendAuthFragment emailSendAuthFragment = new EmailSendAuthFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EmailAuthActivity.EMAIL,email);
        emailSendAuthFragment.setArguments(bundle);
        return emailSendAuthFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle argument = getArguments();
        if(argument != null){
            mEmail = argument.getString(EmailAuthActivity.EMAIL, null);
        }
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mSendAuthCallback = (SendAuthCallback)activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @OnTextChanged(R.id.act_input_email)
    public void onEmailTextChanged(CharSequence text) {
        if (!TextUtils.isEmpty(text)) {
            mBtnLogin.setEnabled(true);
        } else {
            mBtnLogin.setEnabled(false);
        }
    }

    @OnClick({R.id.btn_send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send:
                mEmail = mEtLoginUserName.getText().toString().trim();
                if (checkLoginInput(mEmail)) {
                    saveEamil(mEmail);
                }
                break;
            default:
                break;
        }
    }

    private boolean checkLoginInput(String name) {
        if (!name.matches(Global.EMAIL_MATCH)) {
            makeTextShort(getString(R.string.tip_input_corrent_email));
            mEtLoginUserName.requestFocus();
            return false;
        }
        return true;
    }

    private void saveEamil(String email) {
        UpdateDesignerEmailInfoRequest updateDesignerEmailInfoRequest = new UpdateDesignerEmailInfoRequest();
        updateDesignerEmailInfoRequest.setEmail(email);

        Api.updateDesignerEmailInfo(updateDesignerEmailInfoRequest, new ApiCallback<ApiResponse<String>>() {
            @Override
            public void onPreLoad() {
                showWaitDialog();
            }

            @Override
            public void onHttpDone() {
                hideWaitDialog();
            }

            @Override
            public void onSuccess(ApiResponse<String> apiResponse) {
//                dataManager.setDesigner(mDesigner);
                sendEmailVerify();
            }

            @Override
            public void onFailed(ApiResponse<String> apiResponse) {
                makeTextShort(apiResponse.getErr_msg());
            }

            @Override
            public void onNetworkError(int code) {

            }
        });
    }

    private void sendEmailVerify(){
        SendVerifyEmailRequest sendVerifyEmailRequest = new SendVerifyEmailRequest();

        Api.sendVerifyEmial(sendVerifyEmailRequest, new ApiCallback<ApiResponse<String>>() {
            @Override
            public void onPreLoad() {

            }

            @Override
            public void onHttpDone() {

            }

            @Override
            public void onSuccess(ApiResponse<String> apiResponse) {
                mSendAuthCallback.sendEmailAuthFinish(mEmail);
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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        if(!TextUtils.isEmpty(mEmail)){
            mEtLoginUserName.setText(mEmail);
        }
    }

    public interface SendAuthCallback{
        void sendEmailAuthFinish(String email);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_email_send;
    }
}

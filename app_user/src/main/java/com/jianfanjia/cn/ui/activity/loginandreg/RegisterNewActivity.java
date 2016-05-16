package com.jianfanjia.cn.ui.activity.loginandreg;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.HttpCode;
import com.jianfanjia.api.model.User;
import com.jianfanjia.api.request.guest.RegisterRequest;
import com.jianfanjia.api.request.guest.UpdatePasswordRequest;
import com.jianfanjia.api.request.user.BindPhoneRequest;
import com.jianfanjia.cn.ui.Event.BindingPhoneEvent;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.ui.activity.requirement.PublishRequirementActivity;
import com.jianfanjia.cn.api.Api;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.RegisterInfo;
import com.jianfanjia.cn.business.DataManagerNew;
import com.jianfanjia.cn.constant.IntentConstant;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import com.jianfanjia.cn.ui.activity.welcome.NewUserCollectDecStageActivity;
import de.greenrobot.event.EventBus;

/**
 * @author zhanghao
 * @ClassName: RegisterNewActivity
 * @Description: 注册
 * @date 2015-10-27 下午12:11:23
 */
public class RegisterNewActivity extends BaseActivity {
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

    private void getDataFromIntent() {
        Intent intent = getIntent();
        registerInfo = (RegisterInfo) intent.getSerializableExtra(IntentConstant.REGISTER_INFO);
        requsetCode = intent.getIntExtra(IntentConstant.REGISTER, 0);
    }

    public void initView() {
        if (registerInfo != null) {
            mPhoneView.setText(registerInfo.getPhone());
        }
        mBtnCommit.setEnabled(false);
    }

    @OnTextChanged(R.id.et_verification)
    public void onTextChanged(CharSequence text) {
        if (!TextUtils.isEmpty(text)) {
            mBtnCommit.setEnabled(true);
        } else {
            mBtnCommit.setEnabled(false);
        }
    }

    @OnClick({R.id.head_back_layout, R.id.btn_commit})
    void OnClick(View view) {
        switch (view.getId()) {
            case R.id.btn_commit:
                mVerification = mEtVerification.getText().toString().trim();
                if (checkInput(mVerification)) {
                    registerInfo.setCode(mVerification);
                    actionCommit(registerInfo);
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
    private void actionCommit(RegisterInfo registerInfo) {
        switch (requsetCode) {
            case REGISTER_CODE:
                register(registerInfo);
                break;
            case UPDATE_PSW_CODE:
                updatePassword(registerInfo);
                break;
            case BINDING_PHONE:
                bindPhone(registerInfo);
                break;
        }
    }

    private void register(final RegisterInfo registerInfo) {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setPass(registerInfo.getPass());
        registerRequest.setPhone(registerInfo.getPhone());
        registerRequest.setCode(registerInfo.getCode());
        Api.register(registerRequest, new ApiCallback<ApiResponse<User>>() {
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
                loginUserBean.setPass(registerInfo.getPass());
                DataManagerNew.loginSuccess(loginUserBean);

                startActivity(NewUserCollectDecStageActivity.class);
                appManager.finishActivity(RegisterNewActivity.this);
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

    private void updatePassword(RegisterInfo registerInfo) {
        UpdatePasswordRequest updatePasswordRequest = new UpdatePasswordRequest();
        updatePasswordRequest.setCode(registerInfo.getCode());
        updatePasswordRequest.setPhone(registerInfo.getPhone());
        updatePasswordRequest.setPass(registerInfo.getPass());
        Api.updatePassword(updatePasswordRequest, new ApiCallback<ApiResponse<String>>() {
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
                startActivity(LoginNewActivity.class);
                appManager.finishActivity(RegisterNewActivity.this);
            }

            @Override
            public void onFailed(ApiResponse<String> apiResponse) {
                makeTextShort(apiResponse.getErr_msg());
            }

            @Override
            public void onNetworkError(int code) {
                makeTextShort(HttpCode.NO_NETWORK_ERROR_MSG);
            }
        });
    }

    private void bindPhone(final RegisterInfo registerInfo) {
        BindPhoneRequest bindPhoneReques = new BindPhoneRequest();
        bindPhoneReques.setPhone(registerInfo.getPhone());
        bindPhoneReques.setCode(registerInfo.getCode());
        Api.bindPhone(bindPhoneReques, new ApiCallback<ApiResponse<String>>() {
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
                dataManager.setAccount(registerInfo.getPhone());
                if (getIntent().getExtras().getInt(IntentConstant.BINDING_PHONE_INTENT) == IntentConstant
                        .BINDING_PHONE_REQUIREMENT) {//发布需求就导向发布需求
                    startActivity(PublishRequirementActivity.class);
                } else {
                    EventBus.getDefault().post(new BindingPhoneEvent(registerInfo.getPhone()));
                }
                appManager.finishActivity(RegisterNewActivity.this);
            }

            @Override
            public void onFailed(ApiResponse<String> apiResponse) {
                makeTextShort(apiResponse.getErr_msg());
            }

            @Override
            public void onNetworkError(int code) {
                makeTextShort(HttpCode.NO_NETWORK_ERROR_MSG);
            }
        });
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
    public int getLayoutId() {
        return R.layout.activity_register_new;
    }
}

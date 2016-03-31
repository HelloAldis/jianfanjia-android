package com.jianfanjia.cn.designer.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.model.Designer;
import com.jianfanjia.api.request.guest.LoginRequest;
import com.jianfanjia.api.request.guest.SendVerificationRequest;
import com.jianfanjia.api.request.guest.VerifyPhoneRequest;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.api.Api;
import com.jianfanjia.cn.designer.base.BaseActivity;
import com.jianfanjia.cn.designer.bean.RegisterInfo;
import com.jianfanjia.cn.designer.cache.DataManagerNew;
import com.jianfanjia.cn.designer.config.Global;
import com.jianfanjia.cn.designer.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.designer.tools.LogTool;

/**
 * @author fengliang
 * @ClassName: LoginActivity
 * @Description: 登录
 * @date 2015-8-18 下午12:11:23
 */
public class LoginNewActivity extends BaseActivity implements
        ApiUiUpdateListener, GestureDetector.OnGestureListener {
    private static final String TAG = LoginNewActivity.class.getName();
    private static final int LOGIN = 0;
    private static final int REGISER = 1;
    private static final float Alpha1 = 0.5f;
    private static final float Alpha2 = 1.0f;
    @Bind(R.id.login_layout)
    RelativeLayout loginLayout;
    @Bind(R.id.forget_psw_layout)
    RelativeLayout registerLayout;
    @Bind(R.id.act_login)
    TextView loginTitle;
    @Bind(R.id.act_register)
    TextView registerTitle;
    @Bind(R.id.act_title_layout)
    RelativeLayout titleLayout;

    @Bind(R.id.content_layout)
    RelativeLayout contentLayout;

    @Bind(R.id.login_register_layout)
    RelativeLayout loginRegisterLayout;

    @Bind(R.id.act_viewflipper)
    ViewFlipper viewFlipper;

    @Bind(R.id.act_forget_psw_input_phone)
    EditText mEtRegisterUserName = null;// 注册用户名输入框
    @Bind(R.id.act_forget_psw_input_password)
    EditText mEtRegisterPassword = null;// 注册用户密码输入框
    @Bind(R.id.act_login_input_phone)
    EditText mEtLoginUserName = null;// 用户名输入框
    @Bind(R.id.act_login_input_password)
    EditText mEtLoginPassword = null;// 用户密码输入框
    @Bind(R.id.btn_login)
    Button mBtnLogin = null;// 登录按钮
    @Bind(R.id.btn_next)
    Button mBtnNext = null;// 下一步
    @Bind(R.id.act_forget_password)
    TextView mForgetPswView = null;//忘记密码
    @Bind(R.id.act_login_input_password_delete)
    ImageView loginInputPasswordDelete;
    @Bind(R.id.act_login_input_phone_delete)
    ImageView loginInputPhoneDelete;
    @Bind(R.id.act_forget_psw_input_password_delete)
    ImageView registerInputPasswordDelete;
    @Bind(R.id.act_forget_psw_input_phone_delete)
    ImageView registerInputPhoneDelete;

    private String mUserName = null;// 用户名
    private String mPassword = null;// 密码

    private GestureDetector mGestureDetector;
    private int currentPage = LOGIN;


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
        boolean flag = intent.getBooleanExtra(Global.ISREGIISTER, false);
        if (flag) {
            LogTool.d(TAG, "showregister");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    showRegister();
                }
            }, 300);
        } else {
//            mEtLoginUserName.requestFocus();
        }
    }

    public void initView() {
        controlKeyboardLayout(contentLayout, mBtnLogin);

        mGestureDetector = new GestureDetector(this, this);
        registerTitle.setAlpha(Alpha1);
        loginTitle.setAlpha(Alpha2);
        registerTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        loginTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);

        mBtnLogin.setEnabled(false);
        mBtnNext.setEnabled(false);

        mEtRegisterUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                LogTool.d(TAG, "registerlogin afterTextChanged");
                String text = s.toString();
                if (!TextUtils.isEmpty(text) && !TextUtils.isEmpty(mEtRegisterPassword.getText().toString())) {
                    mBtnNext.setEnabled(true);
                } else {
                    mBtnNext.setEnabled(false);
//                    verifyPhone(text);
                }
            }
        });
        mEtRegisterPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                LogTool.d(TAG, "registerpassword afterTextChanged");
                String text = s.toString();
                if (!TextUtils.isEmpty(text) && !TextUtils.isEmpty(mEtRegisterUserName.getText().toString())) {
                    mBtnNext.setEnabled(true);
                } else {
                    mBtnNext.setEnabled(false);
                }
            }
        });
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

    int scrollHeight;

    private void controlKeyboardLayout(final View root, final View scrollToView) {
        root.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        Rect rect = new Rect();
                        // 获取root在窗体的可视区域
                        root.getWindowVisibleDisplayFrame(rect);
                        // 获取root在窗体的不可视区域高度(被其他View遮挡的区域高度)
                        LogTool.d(TAG, "rect.bottom =" + rect.bottom + " rect.top" + rect.top);
                        int rootInvisibleHeight = root.getRootView()
                                .getHeight() - (rect.bottom - rect.top);
                        // 若不可视区域高度大于100，则键盘显示
                        int[] location = new int[2];
                        // 获取scrollToView在窗体的坐标
                        scrollToView.getLocationInWindow(location);
                        // 计算root滚动高度，使scrollToView在可见区域
                        LogTool.d(TAG, "scrollHeight =" + scrollHeight);
                        if (rootInvisibleHeight > 100) {
                            LogTool.d(TAG, "键盘显示");
                            scrollHeight = (location[1] + scrollToView
                                    .getHeight()) - (rect.bottom);
                            if (scrollHeight > 0) {
                                int traY = root.getTop() - scrollHeight;
                                root.animate().y(traY).setDuration(100).start();
                            }
                        } else {
                            // 键盘隐藏
                            LogTool.d(TAG, "键盘隐藏");
                            root.animate().y(root.getTop()).setDuration(100).start();
                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @OnClick({R.id.btn_login, R.id.btn_next, R.id.act_forget_password, R.id.act_login, R.id.act_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                mUserName = mEtLoginUserName.getText().toString().trim();
                mPassword = mEtLoginPassword.getText().toString().trim();
                if (checkLoginInput(mUserName, mPassword)) {
                    login(mUserName, mPassword);
                }
                break;
            case R.id.btn_next:
                mUserName = mEtRegisterUserName.getText().toString().trim();
                mPassword = mEtRegisterPassword.getText().toString().trim();
                if (checkRegisterInput(mUserName, mPassword)) {
                    verifyPhone(mUserName);
                }
                break;
            case R.id.act_forget_password:
                startActivity(ForgetPswActivity.class);
                break;
            case R.id.act_login:
                if (currentPage == REGISER) {
                    showLogin();
                }
                break;
            case R.id.act_register:
                if (currentPage == LOGIN) {
                    showRegister();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    private boolean checkRegisterInput(String name, String password) {
        if (TextUtils.isEmpty(name)) {
            makeTextShort(getResources().getString(
                    R.string.tip_please_input_username));
            mEtRegisterUserName.requestFocus();
            return false;
        }
        if (!name.matches(Global.PHONE_MATCH)) {
            makeTextShort(getString(R.string.tip_input_corrent_phone));
            mEtRegisterUserName.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            makeTextShort(getResources().getString(
                    R.string.tip_please_input_password));
            mEtRegisterPassword.requestFocus();
            return false;
        }
        if (!password.matches(Global.PASSWORD_MATCH)) {
            makeTextShort(getString(R.string.tip_input_correct_password));
            mEtRegisterPassword.requestFocus();
            return false;
        }
        return true;
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

    private void verifyPhone(final String phone) {
        VerifyPhoneRequest verifyPhoneRequest = new VerifyPhoneRequest();
        verifyPhoneRequest.setPhone(phone);
        Api.verifyPhone(verifyPhoneRequest, new ApiCallback<ApiResponse<String>>() {
            @Override
            public void onPreLoad() {

            }

            @Override
            public void onHttpDone() {

            }

            @Override
            public void onSuccess(ApiResponse<String> apiResponse) {
                sendVerification(mUserName, mPassword);
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

    /**
     * 发送验证码
     *
     * @param name
     * @param password
     */
    private void sendVerification(final String name, final String password) {
        SendVerificationRequest sendVerificationRequest = new SendVerificationRequest();
        sendVerificationRequest.setPhone(name);
        Api.sendVerification(sendVerificationRequest, new ApiCallback<ApiResponse<String>>() {
            @Override
            public void onPreLoad() {

            }

            @Override
            public void onHttpDone() {

            }

            @Override
            public void onSuccess(ApiResponse<String> apiResponse) {
                RegisterInfo registerInfo = new RegisterInfo();
                registerInfo.setPass(password);
                registerInfo.setPhone(name);
                Bundle registerBundle = new Bundle();
                registerBundle.putSerializable(Global.REGISTER_INFO, registerInfo);
                registerBundle.putInt(Global.REGISTER, RegisterNewActivity.REGISTER_CODE);
                startActivity(RegisterNewActivity.class, registerBundle);
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
                hideWaitDialog();
            }

            @Override
            public void onSuccess(ApiResponse<Designer> apiResponse) {

                Designer designer = apiResponse.getData();
                designer.setPass(password);
                DataManagerNew.loginSuccess(designer);

                startActivity(MainActivity.class);
                appManager.finishActivity(LoginNewActivity.this);
            }

            @Override
            public void onFailed(ApiResponse<Designer> apiResponse) {
                makeTextShort(apiResponse.getErr_msg());
            }

            @Override
            public void onNetworkError(int code) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        appManager.finishAllActivity();
    }

    void showRegister() {
        translateAnimationToLeft(titleLayout);
        viewFlipper.showNext();
    }

    void showLogin() {
        translateAnimationToRight(titleLayout);
        viewFlipper.showNext();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (velocityX < -200 && currentPage == LOGIN) {
            showRegister();
            return true;
        } else if (velocityX > 200 && currentPage == REGISER) {
            showLogin();
            return true;
        }
        return false;
    }

    public void translateAnimationToLeft(View view) {
        int[] location = new int[2];
        view.getLocationInWindow(location);
        float currentX = view.getX();
        float currentY = view.getY();
        LogTool.d("translateAnimationToLeft", "currentY = " + currentY);
        PropertyValuesHolder p1 = PropertyValuesHolder.ofFloat("x", currentX, currentX - TypedValue.applyDimension
                (TypedValue.COMPLEX_UNIT_DIP, 124, getResources().getDisplayMetrics()));
        PropertyValuesHolder p2 = PropertyValuesHolder.ofFloat("y", currentY, currentY);
        ObjectAnimator objectAnimator = ObjectAnimator
                .ofPropertyValuesHolder(view, p1, p2)
                .setDuration(200);
        objectAnimator.start();
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                loginTitle.setAlpha(Alpha1);
                registerTitle.setAlpha(Alpha2);
                registerTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                loginTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                currentPage = REGISER;
//                mEtRegisterUserName.requestFocus();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public void translateAnimationToRight(View view) {
        float currentX = view.getTranslationX();
        LogTool.d("translateAnimationToRight", "currentX = " + currentX);
        ObjectAnimator objectAnimator = ObjectAnimator
                .ofFloat(view, "x", currentX, currentX + TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 124,
                        getResources().getDisplayMetrics()))
                .setDuration(200);
        objectAnimator.start();
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                registerTitle.setAlpha(Alpha1);
                loginTitle.setAlpha(Alpha2);
                registerTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                loginTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                currentPage = LOGIN;
//                mEtLoginUserName.requestFocus();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login_new;
    }

}

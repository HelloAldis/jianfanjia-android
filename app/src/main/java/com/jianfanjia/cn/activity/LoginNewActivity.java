package com.jianfanjia.cn.activity;

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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.igexin.sdk.PushManager;
import com.jianfanjia.cn.base.BaseAnnotationActivity;
import com.jianfanjia.cn.bean.RegisterInfo;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.NetTool;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * @author fengliang
 * @ClassName: LoginActivity
 * @Description: 登录
 * @date 2015-8-18 下午12:11:23
 */
@EActivity(R.layout.activity_login_new)
public class LoginNewActivity extends BaseAnnotationActivity implements
        ApiUiUpdateListener, GestureDetector.OnGestureListener {
    private static final String TAG = LoginNewActivity.class.getName();
    private static final int LOGIN = 0;
    private static final int REGISER = 1;
    private static final float Alpha1 = 0.5f;
    private static final float Alpha2 = 1.0f;
    @ViewById(R.id.login_layout)
    RelativeLayout loginLayout;
    @ViewById(R.id.register_layout)
    RelativeLayout registerLayout;
    @ViewById(R.id.act_login)
    TextView loginTitle;
    @ViewById(R.id.act_register)
    TextView registerTitle;
    @ViewById(R.id.act_title_layout)
    RelativeLayout titleLayout;

    @ViewById(R.id.content_layout)
    RelativeLayout contentLayout;

    @ViewById(R.id.login_register_layout)
    RelativeLayout loginRegisterLayout;

    @ViewById(R.id.act_viewflipper)
    ViewFlipper viewFlipper;

    @ViewById(R.id.act_register_input_phone)
    EditText mEtRegisterUserName = null;// 注册用户名输入框
    @ViewById(R.id.act_register_input_password)
    EditText mEtRegisterPassword = null;// 注册用户密码输入框
    @ViewById(R.id.act_login_input_phone)
    EditText mEtLoginUserName = null;// 用户名输入框
    @ViewById(R.id.act_login_input_password)
    EditText mEtLoginPassword = null;// 用户密码输入框
    @ViewById(R.id.btn_login)
    Button mBtnLogin = null;// 登录按钮
    @ViewById(R.id.btn_next)
    Button mBtnNext = null;// 下一步
    @ViewById(R.id.act_forget_password)
    TextView mForgetPswView = null;//忘记密码
    @ViewById(R.id.act_login_input_password_delete)
    ImageView loginInputPasswordDelete;
    @ViewById(R.id.act_login_input_phone_delete)
    ImageView loginInputPhoneDelete;
    @ViewById(R.id.act_register_input_password_delete)
    ImageView registerInputPasswordDelete;
    @ViewById(R.id.act_register_input_phone_delete)
    ImageView registerInputPhoneDelete;

    private String mUserName = null;// 用户名
    private String mPassword = null;// 密码

    private GestureDetector mGestureDetector;
    int currentPage = LOGIN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    public void initView() {
        controlKeyboardLayout(contentLayout, mBtnLogin);

        mGestureDetector = new GestureDetector(this, this);
        registerTitle.setAlpha(Alpha1);
        loginTitle.setAlpha(Alpha2);
        registerTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        loginTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);

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
        }

        mEtRegisterUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                if(text == null || !text.matches(Global.PHONE_MATCH)){
                    registerInputPhoneDelete.setVisibility(View.VISIBLE);
                }else{
                    verifyPhone(text);
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
                String text = s.toString();
                if(text == null || !text.matches(Global.PASSWORD_MATCH)){
                    registerInputPasswordDelete.setVisibility(View.VISIBLE);
                }else{
                    registerInputPasswordDelete.setVisibility(View.GONE);
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
                if(text == null || !text.matches(Global.PHONE_MATCH)){
                    loginInputPhoneDelete.setVisibility(View.VISIBLE);
                }else{
                    loginInputPhoneDelete.setVisibility(View.GONE);
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
                if(text == null || !text.matches(Global.PASSWORD_MATCH)){
                    loginInputPasswordDelete.setVisibility(View.VISIBLE);
                }else{
                    loginInputPasswordDelete.setVisibility(View.GONE);
                }
            }
        });
    }

    private void controlKeyboardLayout(final View root,final View scrollToView){
        root.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        Rect rect = new Rect();
                        // 获取root在窗体的可视区域
                        root.getWindowVisibleDisplayFrame(rect);
                        // 获取root在窗体的不可视区域高度(被其他View遮挡的区域高度)
                        int rootInvisibleHeight = root.getRootView()
                                .getHeight() - rect.bottom;
                        // 若不可视区域高度大于100，则键盘显示
                        int[] location = new int[2];
                        // 获取scrollToView在窗体的坐标
                        scrollToView.getLocationInWindow(location);
                        // 计算root滚动高度，使scrollToView在可见区域
                        int srollHeight = (location[1] + scrollToView
                                .getHeight()) - rect.bottom;
                        if (rootInvisibleHeight > 100) {
//                            root.scrollTo(0, srollHeight);
                            root.animate().translationY(-srollHeight).setDuration(100).start();
                        } else {
                            // 键盘隐藏
                            root.animate().translationY(0).setDuration(100).start();
//                            root.scrollTo(0, 0);
                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Click({R.id.btn_login, R.id.btn_next, R.id.act_forget_password, R.id.act_login, R.id.act_register})
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
                    sendVerification(mUserName, mPassword);
                }
                break;
            case R.id.act_forget_password:
                startActivity(ForgetPswActivity_.class);
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
        mEtRegisterUserName.setText("");
        mEtRegisterPassword.setText("");
        mEtLoginUserName.setText("");
        mEtLoginPassword.setText("");
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

    private void verifyPhone(final String phone){
        if(NetTool.isNetworkAvailable(this)){
            JianFanJiaClient.verifyPhone(this, phone, new ApiUiUpdateListener() {
                @Override
                public void preLoad() {

                }

                @Override
                public void loadSuccess(Object data) {
                    registerInputPhoneDelete.setVisibility(View.GONE);
                }

                @Override
                public void loadFailture(String error_msg) {
                    makeTextShort(error_msg);
                }
            },this);
        }else{
            makeTextShort(getString(R.string.tip_internet_not));
        }
    }

    /**
     * 发送验证码
     *
     * @param name
     * @param password
     */
    private void sendVerification(final String name, final String password) {
        if (NetTool.isNetworkAvailable(this)) {
            JianFanJiaClient.send_verification(this, name, new ApiUiUpdateListener() {
                @Override
                public void preLoad() {

                }

                @Override
                public void loadSuccess(Object data) {
                    RegisterInfo registerInfo = new RegisterInfo();
                    registerInfo.setPass(password);
                    registerInfo.setPhone(name);
                    Bundle registerBundle = new Bundle();
                    registerBundle.putSerializable(Global.REGISTER_INFO, registerInfo);
                    registerBundle.putInt(Global.REGISTER, RegisterNewActivity.REGISTER_CODE);
                    startActivity(RegisterNewActivity_.class, registerBundle);
                }

                @Override
                public void loadFailture(String error_msg) {
                    makeTextLong(error_msg);
                }
            }, this);
        } else {
            makeTextLong(getString(R.string.tip_internet_not));
        }
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
    public void loadSuccess(Object data) {
        super.loadSuccess(data);
        PushManager.getInstance().initialize(getApplicationContext());
        startActivity(MainActivity.class);
        finish();
    }

    @Override
    public void loadFailture(String error_msg) {
        super.loadFailture(error_msg);
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
        PropertyValuesHolder p1 = PropertyValuesHolder.ofFloat("x", currentX, currentX - TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 124, getResources().getDisplayMetrics()));
        PropertyValuesHolder p2 = PropertyValuesHolder.ofFloat("y",currentY,currentY);
        ObjectAnimator objectAnimator = ObjectAnimator
                .ofPropertyValuesHolder(view, p1,p2)
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
                .ofFloat(view, "x", currentX, currentX + TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 124, getResources().getDisplayMetrics()))
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
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public TranslateAnimation getToLeft() {
        TranslateAnimation translateAnimation = new TranslateAnimation(0, -TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 124, getResources().getDisplayMetrics()), 0, 0);
        translateAnimation.setDuration(200);
        translateAnimation.setFillAfter(true);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        return translateAnimation;
    }

    public TranslateAnimation getToRight() {
        TranslateAnimation translateAnimation = new TranslateAnimation(-TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 124, getResources().getDisplayMetrics()), 0, 0, 0);
        translateAnimation.setDuration(200);
        translateAnimation.setFillAfter(true);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                registerTitle.setAlpha(Alpha1);
                loginTitle.setAlpha(Alpha2);
                registerTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                loginTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                currentPage = LOGIN;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        return translateAnimation;
    }

    public AlphaAnimation get0To1() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(100);
        alphaAnimation.setFillAfter(true);
        return alphaAnimation;
    }

    public AlphaAnimation get1To0() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setDuration(100);
        alphaAnimation.setFillAfter(true);
        return alphaAnimation;
    }
}

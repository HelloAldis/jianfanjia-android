package com.jianfanjia.cn.activity;

import android.graphics.Rect;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.http.LoadClientHelper;
import com.jianfanjia.cn.http.request.LoginRequest;
import com.jianfanjia.cn.interf.LoadDataListener;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.NetTool;

/**
 * 
 * @ClassName: LoginActivity
 * @Description: 登录
 * @author fengliang
 * @date 2015-8-18 下午12:11:23
 * 
 */         
public class LoginActivity extends BaseActivity implements OnClickListener{
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
		controlKeyboardLayout(loginLayout, mBtnLogin);
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
		if (!NetTool.isNetworkAvailable(LoginActivity.this)) {
			makeTextShort(getResources().getString(R.string.tip_no_internet));
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
//		dataManager.login(name, password, this);
		LoginRequest loginRequest = new LoginRequest(this, name, password);
		LoadClientHelper.login(this, loginRequest, this);
	}

	@Override
	public void loadSuccess() {
		super.loadSuccess();
		startActivity(MainActivity.class);
		finish();
	}

	/**
	 * @param root
	 *            最外层布局，需要调整的布局
	 * @param scrollToView
	 *            被键盘遮挡的scrollToView，滚动root,使scrollToView在root可视区域的底部
	 */
	private void controlKeyboardLayout(final View root, final View scrollToView) {
		root.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						Rect rect = new Rect();
						// 获取root在窗体的可视区域
						root.getWindowVisibleDisplayFrame(rect);
						// 获取root在窗体的不可视区域高度(被其他View遮挡的区域高度)
						int rootInvisibleHeight = root.getRootView()
								.getHeight() - rect.bottom;
						// 若不可视区域高度大于100，则键盘显示
						if (rootInvisibleHeight > 100) {
							int[] location = new int[2];
							// 获取scrollToView在窗体的坐标
							scrollToView.getLocationInWindow(location);
							// 计算root滚动高度，使scrollToView在可见区域
							int srollHeight = (location[1] + scrollToView
									.getHeight()) - rect.bottom;
							root.scrollTo(0, srollHeight);
						} else {
							// 键盘隐藏
							root.scrollTo(0, 0);
						}
					}
				});
	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_login;
	}

}

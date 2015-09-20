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
 * @Description: ��¼
 * @author fengliang
 * @date 2015-8-18 ����12:11:23
 * 
 */         
public class LoginActivity extends BaseActivity implements OnClickListener{
	private static final String TAG = LoginActivity.class.getName();
	private RelativeLayout loginLayout = null;
	private EditText mEtUserName = null;// �û��������
	private EditText mEtPassword = null;// �û����������
	private Button mBtnLogin = null;// ��¼��ť
	private TextView mForgetPswView = null;
	private TextView mRegisterView = null;// �������û�ע��
	private String mUserName = null;// �û���
	private String mPassword = null;// ����

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
	 * ��¼
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
	 *            ����㲼�֣���Ҫ�����Ĳ���
	 * @param scrollToView
	 *            �������ڵ���scrollToView������root,ʹscrollToView��root��������ĵײ�
	 */
	private void controlKeyboardLayout(final View root, final View scrollToView) {
		root.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						Rect rect = new Rect();
						// ��ȡroot�ڴ���Ŀ�������
						root.getWindowVisibleDisplayFrame(rect);
						// ��ȡroot�ڴ���Ĳ���������߶�(������View�ڵ�������߶�)
						int rootInvisibleHeight = root.getRootView()
								.getHeight() - rect.bottom;
						// ������������߶ȴ���100���������ʾ
						if (rootInvisibleHeight > 100) {
							int[] location = new int[2];
							// ��ȡscrollToView�ڴ��������
							scrollToView.getLocationInWindow(location);
							// ����root�����߶ȣ�ʹscrollToView�ڿɼ�����
							int srollHeight = (location[1] + scrollToView
									.getHeight()) - rect.bottom;
							root.scrollTo(0, srollHeight);
						} else {
							// ��������
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

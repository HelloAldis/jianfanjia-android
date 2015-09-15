package com.jianfanjia.cn.activity;

import java.util.Calendar;

import org.apache.http.Header;
import org.apache.http.client.CookieStore;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jianfanjia.cn.AppConfig;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.LoginUserBean;
import com.jianfanjia.cn.cache.DataManager;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.http.HttpRestClient;
import com.jianfanjia.cn.http.JianFanJiaApiClient;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.NetTool;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;

/**
 * 
 * @ClassName: LoginActivity
 * @Description: ��¼
 * @author fengliang
 * @date 2015-8-18 ����12:11:23
 * 
 */
public class LoginActivity extends BaseActivity implements OnClickListener {
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
		mUserName = sharedPrefer.getValue(Constant.ACCOUNT, null);
		mPassword = sharedPrefer.getValue(Constant.PASSWORD, null);
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
		JianFanJiaApiClient.login(LoginActivity.this, name, password,
				new JsonHttpResponseHandler() {
					@Override
					public void onStart() {
						LogTool.d(TAG, "onStart()");
						showWaitDialog(R.string.loging);
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						LogTool.d(TAG, "JSONObject response:" + response);
						try {
							for (Header header : headers) {
								Log.i(TAG,
										header.getName() + "----"
												+ header.getValue());
							}
							if (response.has(Constant.DATA)) {
								appConfig.savaLastLoginTime(Calendar.getInstance().getTimeInMillis());
								hideWaitDialog();
								makeTextShort(getString(R.string.login_success));
								LoginUserBean loginUserBean = JsonParser
										.jsonToBean(response.get(Constant.DATA)
												.toString(),
												LoginUserBean.class);
								LogTool.d(TAG, "loginUserBean:" + loginUserBean);
								DataManager.getInstance().saveLoginUserInfo(
										loginUserBean);
								startActivity(MainActivity.class);
								dataManager.setLogin(true);
								finish();
							} else if (response.has(Constant.ERROR_MSG)) {
								hideWaitDialog();
								makeTextLong(response.get(Constant.ERROR_MSG)
										.toString());
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							makeTextLong(getString(R.string.load_failure));
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						LogTool.d(TAG,
								"Throwable throwable:" + throwable.toString());
						hideWaitDialog();
						makeTextLong(getString(R.string.tip_no_internet));
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						LogTool.d(TAG, "throwable:" + throwable);
						hideWaitDialog();
						makeTextLong(getString(R.string.tip_no_internet));
					};
				});
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

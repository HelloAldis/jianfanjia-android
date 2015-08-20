package com.jianfanjia.cn.activity;

import org.apache.http.Header;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.http.JianFanJiaApiClient;
import com.jianfanjia.cn.tools.TDevice;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * 
 * @ClassName: LoginActivity
 * @Description: 登录
 * @author fengliang
 * @date 2015-8-18 下午12:11:23
 * 
 */
public class LoginActivity extends BaseActivity implements OnClickListener {

	public static final int REQUEST_CODE_INIT = 0;
	private static final String BUNDLE_KEY_REQUEST_CODE = "BUNDLE_KEY_REQUEST_CODE";
	protected static final String TAG = LoginActivity.class.getSimpleName();

	EditText mEtUserName;// 用户名输入框
	EditText mEtPassword;// 用户密码输入框
	Button mBtnLogin;// 登录按钮

	private String mUserName;// 用户名
	private String mPassword;// 密码

	@Override
	public void initView() {
		mEtUserName = (EditText) findViewById(R.id.et_username);
		mEtPassword = (EditText) findViewById(R.id.et_password);
		mBtnLogin = (Button) findViewById(R.id.btn_login);
	}

	@Override
	public void setListener() {
		mBtnLogin.setOnClickListener(this);
	}

	@Override
	public int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.activity_login;
	}

	// 处理登录过程
	private void handleLogin() {
		/*if (!prepareForLogin()) {
			return;
		}*/
		// if the data has ready
		mUserName = mEtUserName.getText().toString();
		mPassword = mEtPassword.getText().toString();

		JianFanJiaApiClient.login("18107218595", "654321",
				asyncHttpResponseHandler);
	}

	private JsonHttpResponseHandler asyncHttpResponseHandler = new JsonHttpResponseHandler() {
		
		@Override
		public void onSuccess(int statusCode, Header[] headers, org.json.JSONObject response) {
			makeTextLong(response.toString());
		};
		
		@Override
		public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
			makeTextLong(responseString);
		};
		
	};

	// 客户端对登录数据的验证
	private boolean prepareForLogin() {

		if (!TDevice.hasInternet()) {
			makeTextShort(getResources().getString(R.string.tip_no_internet));
			return false;
		}

		String uName = mEtUserName.getText().toString();
		if (TextUtils.isEmpty(uName)) {
			makeTextShort(getResources().getString(
					R.string.tip_please_input_username));
			mEtUserName.requestFocus();
			return false;
		}
		String pwd = mEtPassword.getText().toString();
		if (TextUtils.isEmpty(pwd)) {
			makeTextShort(getResources().getString(
					R.string.tip_please_input_password));
			mEtPassword.requestFocus();
			return false;
		}
		return true;
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		switch (id) {
		case R.id.btn_login:
			handleLogin();
			break;
		default:
			break;
		}
	}

}

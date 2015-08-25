package com.jianfanjia.cn.activity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.LoginUserBean;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.http.JianFanJiaApiClient;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.NetTool;
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
	private static final String TAG = LoginActivity.class.getClass().getName();
	private EditText mEtUserName;// 用户名输入框
	private EditText mEtPassword;// 用户密码输入框
	private Button mBtnLogin;// 登录按钮
	private TextView mRegisterView;// 导航到用户注册
	private String mUserName;// 用户名
	private String mPassword;// 密码

	@Override
	public void initView() {
		mEtUserName = (EditText) findViewById(R.id.et_username);
		mEtPassword = (EditText) findViewById(R.id.et_password);
		mBtnLogin = (Button) findViewById(R.id.btn_login);
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
	}

	@Override
	public void setListener() {
		mBtnLogin.setOnClickListener(this);
		mRegisterView.setOnClickListener(this);
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
			Intent intent = new Intent(LoginActivity.this,
					RegisterActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.fragment_slide_right_enter,
					R.anim.fragment_slide_left_exit);
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
		JianFanJiaApiClient.login(LoginActivity.this, name, password,
				new JsonHttpResponseHandler() {
					@Override
					public void onStart() {
						LogTool.d(TAG, "onStart()");
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						LogTool.d(TAG, "JSONObject response:" + response);
						try {
							if (response.has(Constant.DATA)) {
								makeTextShort(getString(R.string.login_success));
								LoginUserBean loginUserBean = jsonParser
										.jsonToBean(response.get(Constant.DATA)
												.toString(),
												LoginUserBean.class);
								saveLoginUserInfo(loginUserBean);
								startActivity(MainActivity.class);
								finish();
							} else if (response.has(Constant.ERROR_MSG)) {
								makeTextLong(response.get(Constant.ERROR_MSG)
										.toString());
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							makeTextLong(getString(R.string.tip_login_error_for_network));
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						LogTool.d(TAG, "throwable:" + throwable);
						makeTextLong(getString(R.string.tip_login_error_for_network));
					};
				});
	}

	/**
	 * 保存登录成功用户信息
	 * 
	 * @param userBean
	 */
	private void saveLoginUserInfo(LoginUserBean userBean) {
		sharedPrefer.setValue(Constant.ACCOUNT, userBean.getPhone());
		sharedPrefer.setValue(Constant.USERTYPE, userBean.getUsertype());
		sharedPrefer.setValue(Constant.PASSWORD, mPassword);
	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_login;
	}

}

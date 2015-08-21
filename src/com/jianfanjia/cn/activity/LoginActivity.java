package com.jianfanjia.cn.activity;

import org.apache.http.Header;
import org.json.JSONException;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.LoginUserBean;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.http.JianFanJiaApiClient;
import com.jianfanjia.cn.tools.TDevice;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * 
 * @ClassName: LoginActivity
 * @Description: ��¼
 * @author fengliang
 * @date 2015-8-18 ����12:11:23
 * 
 */
public class LoginActivity extends BaseActivity implements OnClickListener {

	public static final int REQUEST_CODE_INIT = 0;
	private static final String BUNDLE_KEY_REQUEST_CODE = "BUNDLE_KEY_REQUEST_CODE";
	protected static final String TAG = LoginActivity.class.getSimpleName();

	EditText mEtUserName;// �û��������
	EditText mEtPassword;// �û����������
	Button mBtnLogin;// ��¼��ť
	
	private TextView mRegisterView;//�������û�ע��
	
	private String mUserName;// �û���
	private String mPassword;// ����

	@Override
	public void initView() {
		mEtUserName = (EditText) findViewById(R.id.et_username);
		mEtPassword = (EditText) findViewById(R.id.et_password);
		mBtnLogin = (Button) findViewById(R.id.btn_login);
		mRegisterView = (TextView)findViewById(R.id.register);

		mUserName = sharedPrefer.getValue(Constant.ACCOUNT, null);
		mPassword = sharedPrefer.getValue(Constant.PASSWORD, null);
		if (mUserName != null) {
			mEtUserName.setText(mUserName);
		}

		if (sharedPrefer.getValue(Constant.PASSWORD, null) != null) {
			mEtPassword.setText(mPassword);
		}
	}

	@Override
	public void setListener() {
		mBtnLogin.setOnClickListener(this);
		mRegisterView.setOnClickListener(this);
	}

	@Override
	public int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.activity_login;
	}

	// �����¼����
	private void handleLogin() {
		if (!prepareForLogin()) {
			return;
		}
		// if the data has ready
		mUserName = mEtUserName.getText().toString();
		mPassword = mEtPassword.getText().toString();

		// mUserName = "18107218595"��password = "654321"
		makeTextShort("mUserName =" + mUserName + "password " + mPassword);

		JianFanJiaApiClient.login(mUserName, mPassword,
				asyncHttpResponseHandler);
	}

	private JsonHttpResponseHandler asyncHttpResponseHandler = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				org.json.JSONObject response) {
			try {
				if (response.has(Constant.DATA)) {
					LoginUserBean loginUserBean = jsonParser.jsonToBean(
							response.get(Constant.DATA).toString(),
							LoginUserBean.class);
					sharedPrefer.setValue(Constant.ACCOUNT, mUserName);
					sharedPrefer.setValue(Constant.PASSWORD, mPassword);

					// �����¼״̬���û�����
					MyApplication.getInstance().setLogin(true);
					MyApplication.getInstance().setUserType(
							Integer.parseInt(loginUserBean.getUsertype()));

					// ��ʾ��¼�ɹ�
					makeTextShort(getString(R.string.login_success));
					
					//�����¼���ת��
					handlerLoginSuccess();
					
				} else if (response.has(Constant.ERROR_MSG)) {
					makeTextLong(response.get(Constant.ERROR_MSG).toString());
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				makeTextLong(getString(R.string.tip_login_error_for_network));
			}
		};

		@Override
		public void onFailure(int statusCode, Header[] headers,
				String responseString, Throwable throwable) {
			makeTextLong(getString(R.string.tip_login_error_for_network));
		};

	};

	// �����¼�ɹ��ĺ�������
	private void handlerLoginSuccess() {
		
	}

	// �ͻ��˶Ե�¼���ݵ���֤
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
		case R.id.register:
			Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
			startActivity(intent);
			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			break;
		default:
			break;
		}
	}

}

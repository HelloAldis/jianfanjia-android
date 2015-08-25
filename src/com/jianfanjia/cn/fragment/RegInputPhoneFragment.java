package com.jianfanjia.cn.fragment;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.http.JianFanJiaApiClient;
import com.jianfanjia.cn.interf.FragmentListener;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.NetTool;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * @version 1.0
 * @Description 输入用户名密码的Fragment
 * @author zhanghao
 * @date 2015-8-21 上午9:15
 * 
 */
public class RegInputPhoneFragment extends BaseFragment {
	private static final String TAG = RegInputPhoneFragment.class.getClass()
			.getName();
	private FragmentListener fragemntListener = null;
	private EditText mUserName = null;// 用户名输入框
	private EditText mPassword = null;// 密码输入框
	private Button nextView = null;// 下一步
	private TextView backView = null;// 返回
	private ImageView indicatorView = null;// 指示器
	private TextView proTipView = null;// 提示操作

	private String mUserNameStr = null;// 用户名
	private String mPasswordStr = null;// 密码

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			fragemntListener = (FragmentListener) activity;
		} catch (ClassCastException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initView(View view) {
		nextView = (Button) view.findViewById(R.id.btn_next);
		backView = (TextView) view.findViewById(R.id.goback);
		mUserName = (EditText) view.findViewById(R.id.et_username);
		mPassword = (EditText) view.findViewById(R.id.et_password);
		indicatorView = (ImageView) view.findViewById(R.id.indicator);
		indicatorView.setImageResource(R.drawable.rounded_register2);
		proTipView = (TextView) view.findViewById(R.id.register_pro);
		proTipView.setText(getString(R.string.input_phone));
	}

	@Override
	public void setListener() {
		nextView.setOnClickListener(this);
		backView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_next:
			mUserNameStr = mUserName.getText().toString().trim();
			mPasswordStr = mPassword.getText().toString().trim();
			if (checkInput(mUserNameStr, mUserNameStr)) {
				fragemntListener.onNext();
				registerInfo.setPhone(mUserNameStr);
				registerInfo.setPass(mPasswordStr);
				sendVerifyCode(mUserNameStr);
			}
			break;
		case R.id.goback:
			fragemntListener.onBack();
			break;
		default:
			break;
		}
	}

	private boolean checkInput(String name, String password) {
		if (TextUtils.isEmpty(name)) {
			makeTextShort(getResources().getString(
					R.string.tip_please_input_username));
			mUserName.requestFocus();
			return false;
		}
		if (TextUtils.isEmpty(password)) {
			makeTextShort(getResources().getString(
					R.string.tip_please_input_password));
			mPassword.requestFocus();
			return false;
		}
		if (!NetTool.isNetworkAvailable(getActivity())) {
			makeTextShort(getResources().getString(R.string.tip_no_internet));
			return false;
		}
		return true;
	}

	/**
	 * 发送验证码
	 * 
	 * @param name
	 * @param password
	 */
	private void sendVerifyCode(String name) {
		JianFanJiaApiClient.send_verification(getActivity(), name,
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
							if (response.has(Constant.SUCCESS_MSG)) {
								makeTextLong(response.get(Constant.SUCCESS_MSG)
										.toString());
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

	@Override
	public int getLayoutId() {
		return R.layout.fragment_register_input_phone;
	}
}

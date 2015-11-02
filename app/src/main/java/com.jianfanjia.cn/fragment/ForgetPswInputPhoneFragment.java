package com.jianfanjia.cn.fragment;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.base.BaseResponse;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.FragmentListener;
import com.jianfanjia.cn.interf.LoadDataListener;
import com.jianfanjia.cn.tools.NetTool;

public class ForgetPswInputPhoneFragment extends BaseFragment {
	private static final String TAG = RegInputPhoneFragment.class.getName();
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
		indicatorView.setImageResource(R.drawable.rounded_forget1);
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
				MyApplication.getInstance().getRegisterInfo()
						.setPhone(mUserNameStr);
				MyApplication.getInstance().getRegisterInfo()
						.setPass(mPasswordStr);
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
			makeTextShort(getResources().getString(R.string.tip_error_internet));
			return false;
		}
		return true;
	}

	/**
	 * 发送验证码
	 * 
	 * @param phone
	 *
	 */
	private void sendVerifyCode(String phone) {
		JianFanJiaClient.send_verification(getActivity(), phone, new LoadDataListener() {
			@Override
			public void preLoad() {

			}

			@Override
			public void loadSuccess(BaseResponse baseResponse) {
				fragemntListener.onNext();
			}

			@Override
			public void loadFailture() {

			}
		},this);
	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_forget_psw_input_phone;
	}

}

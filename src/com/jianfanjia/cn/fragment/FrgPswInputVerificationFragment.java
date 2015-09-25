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
import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.bean.RegisterInfo;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.http.JianFanJiaApiClient;
import com.jianfanjia.cn.interf.FragmentListener;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.NetTool;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * @version 1.0
 * @Description 注册选择角色Fragment
 * @author zhanghao
 * @date 2015-8-21 上午9:15
 * 
 */
public class FrgPswInputVerificationFragment extends BaseFragment {
	private static final String TAG = FrgPswInputVerificationFragment.class
			.getName();
	private FragmentListener fragemntListener = null;
	private Button nextView = null;// 下一步
	private TextView backView = null;// 返回
	private EditText mEdVerif = null;// 验证码输入框
	private ImageView indicatorView = null;// 指示器
	private TextView proTipView = null;// 提示操作

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
		nextView = (Button) view.findViewById(R.id.btn_commit);
		backView = (TextView) view.findViewById(R.id.goback);
		mEdVerif = (EditText) view.findViewById(R.id.et_verification);
		indicatorView = (ImageView) view.findViewById(R.id.indicator);
		indicatorView.setImageResource(R.drawable.rounded_forget2);
		proTipView = (TextView) view.findViewById(R.id.register_pro);
		proTipView.setText(getString(R.string.verification_code_sended));
	}

	@Override
	public void setListener() {
		nextView.setOnClickListener(this);
		backView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_commit:
			String verif = mEdVerif.getText().toString().trim();
			if (checkInput(verif)) {
				MyApplication.getInstance().getRegisterInfo().setCode(verif);
				register(MyApplication.getInstance().getRegisterInfo());
			}
			break;
		case R.id.goback:
			fragemntListener.onBack();
			break;
		default:
			break;
		}
	}

	private boolean checkInput(String verifCode) {
		if (TextUtils.isEmpty(verifCode)) {
			makeTextShort(getResources().getString(R.string.hint_verification));
			mEdVerif.requestFocus();
			return false;
		}
		if (!NetTool.isNetworkAvailable(getActivity())) {
			makeTextShort(getResources().getString(R.string.tip_error_internet));
			return false;
		}
		return true;
	}

	/**
	 * 注册提交
	 * 
	 * @param registerInfo
	 */
	private void register(RegisterInfo registerInfo) {
		JianFanJiaApiClient.register(getActivity(), registerInfo,
				new JsonHttpResponseHandler() {
					@Override
					public void onStart() {
						showWaitDialog(R.string.submiting);
						LogTool.d(TAG, "onStart()");
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						hideWaitDialog();
						LogTool.d(TAG, "JSONObject response:" + response);
						try {
							if (response.has(Constant.SUCCESS_MSG)) {
								makeTextLong(getString(R.string.update_psw_success));
								getActivity().finish();
							} else if (response.has(Constant.ERROR_MSG)) {
								makeTextLong(response.get(Constant.ERROR_MSG)
										.toString());
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							hideWaitDialog();
							makeTextLong(getString(R.string.load_failure));
						}
					}
					
					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						LogTool.d(TAG,
								"Throwable throwable:" + throwable.toString());
						hideWaitDialog();
						makeTextLong(getString(R.string.tip_error_internet));
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						hideWaitDialog();
						LogTool.d(TAG, "throwable:" + throwable);
						makeTextLong(getString(R.string.tip_error_internet));
					};
				});
	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_forget_psw_input_verification;
	}
}

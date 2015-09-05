package com.jianfanjia.cn.fragment;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;
import com.igexin.sdk.PushManager;
import com.jianfanjia.cn.activity.AboutActivity;
import com.jianfanjia.cn.activity.FeedBackActivity;
import com.jianfanjia.cn.activity.LoginActivity;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.http.JianFanJiaApiClient;
import com.jianfanjia.cn.interf.DialogListener;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.dialog.CustomProgressDialog;
import com.jianfanjia.cn.view.dialog.HintDialog;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * 
 * @ClassName: SettingFragment
 * @Description: 设置
 * @author fengliang
 * @date 2015-8-26 下午3:58:10
 * 
 */
public class SettingFragment extends BaseFragment implements
		OnCheckedChangeListener, DialogListener {
	private static final String TAG = SettingFragment.class.getName();
	private CustomProgressDialog progressDialog = null;
	private HintDialog hintDialog = null;
	private RelativeLayout feedbackFragment = null;
	private RelativeLayout aboutFragment = null;
	private ToggleButton toggleButton;
	private RelativeLayout logoutLayout = null;
	private RelativeLayout helpLayout = null;
	private RelativeLayout current_version_layout = null;

	@Override
	public void initView(View view) {
		progressDialog = new CustomProgressDialog(getActivity(), "获取新版本",
				R.style.dialog);
		hintDialog = new HintDialog(getActivity(), R.layout.hint_dialog,
				"退出登录", "确定要退出吗？", R.style.dialog);
		feedbackFragment = (RelativeLayout) view
				.findViewById(R.id.feedback_layout);
		helpLayout = (RelativeLayout) view.findViewById(R.id.help_layout);
		aboutFragment = (RelativeLayout) view.findViewById(R.id.about_layout);
		toggleButton = (ToggleButton) view.findViewById(R.id.mespush_toggle);
		logoutLayout = (RelativeLayout) view.findViewById(R.id.logout_layout);
		current_version_layout = (RelativeLayout) view
				.findViewById(R.id.current_version_layout);
	}

	@Override
	public void setListener() {
		feedbackFragment.setOnClickListener(this);
		aboutFragment.setOnClickListener(this);
		helpLayout.setOnClickListener(this);
		toggleButton.setOnCheckedChangeListener(this);
		logoutLayout.setOnClickListener(this);
		current_version_layout.setOnClickListener(this);
		hintDialog.setListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.feedback_layout:
			startActivity(FeedBackActivity.class);
			break;
		case R.id.about_layout:
			startActivity(AboutActivity.class);
			break;
		case R.id.help_layout:
			break;
		case R.id.logout_layout:
			hintDialog.show();
			break;
		case R.id.current_version_layout:
			checkVersion();
			break;
		default:
			break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		LogTool.d(TAG, "onResume()   " + isOpen);
		if (isOpen) {
			toggleButton.setChecked(true);
		} else {
			toggleButton.setChecked(false);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		LogTool.d(TAG, "onDestroy()");
	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean check) {
		LogTool.d(TAG, "check:" + check);
		shared.setValue(Constant.ISOPEN, check);
		if (check) {

		} else {

		}
	}

	@Override
	public void onPositiveButtonClick() {
		hintDialog.dismiss();
		logout();
	}

	@Override
	public void onNegativeButtonClick() {
		hintDialog.dismiss();
	}

	// 检查版本
	private void checkVersion() {
		JianFanJiaApiClient.checkVersion(getActivity(),
				new JsonHttpResponseHandler() {
					@Override
					public void onStart() {
						LogTool.d(TAG, "onStart()");
						progressDialog.show();
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						LogTool.d(TAG, "JSONObject response:" + response);
						progressDialog.dismiss();
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						LogTool.d(TAG,
								"Throwable throwable:" + throwable.toString());
						makeTextLong(getString(R.string.tip_login_error_for_network));
						progressDialog.dismiss();
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						LogTool.d(TAG, "throwable:" + throwable);
						makeTextLong(getString(R.string.tip_login_error_for_network));
						progressDialog.dismiss();
					};
				});
	}

	// 退出登录
	private void logout() {
		JianFanJiaApiClient.logout(getActivity(),
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
								PushManager.getInstance().stopService(
										getActivity());// 完全终止SDK的服务
								startActivity(LoginActivity.class);
								getActivity().finish();
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
							Throwable throwable, JSONObject errorResponse) {
						LogTool.d(TAG,
								"Throwable throwable:" + throwable.toString());
						makeTextLong(getString(R.string.tip_login_error_for_network));
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
		return R.layout.fragment_setting;
	}

}

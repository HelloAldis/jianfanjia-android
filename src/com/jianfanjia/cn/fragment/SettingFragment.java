package com.jianfanjia.cn.fragment;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;
import com.jianfanjia.cn.activity.AboutActivity;
import com.jianfanjia.cn.activity.FeedBackActivity;
import com.jianfanjia.cn.activity.LoginActivity;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.http.JianFanJiaApiClient;
import com.jianfanjia.cn.tools.LogTool;
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
		OnCheckedChangeListener {
	private static final String TAG = SettingFragment.class.getName();
	private RelativeLayout feedbackFragment = null;
	private RelativeLayout aboutFragment = null;
	private ToggleButton toggleButton;
	private RelativeLayout logoutLayout = null;
	private RelativeLayout current_version_layout = null;

	@Override
	public void initView(View view) {
		feedbackFragment = (RelativeLayout) view
				.findViewById(R.id.feedback_layout);
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
		toggleButton.setOnCheckedChangeListener(this);
		logoutLayout.setOnClickListener(this);
		current_version_layout.setOnClickListener(this);
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
		case R.id.logout_layout:
			logout();
			break;
		case R.id.current_version_layout:
			checkVersion();
			break;
		default:
			break;
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean check) {

	}

	// 检查版本
	private void checkVersion() {
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

	// 登出
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

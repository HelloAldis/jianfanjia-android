package com.jianfanjia.cn.fragment;

import java.io.File;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.igexin.sdk.PushManager;
import com.jianfanjia.cn.activity.AboutActivity;
import com.jianfanjia.cn.activity.FeedBackActivity;
import com.jianfanjia.cn.activity.LoginActivity;
import com.jianfanjia.cn.activity.NavigateActivity;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.ShareActivity;
import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.http.JianFanJiaApiClient;
import com.jianfanjia.cn.interf.DialogListener;
import com.jianfanjia.cn.tools.FileUtil;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.dialog.CommonDialog;
import com.jianfanjia.cn.view.dialog.DialogHelper;
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
	private RelativeLayout helpLayout = null;
	private RelativeLayout current_version_layout = null;
	private RelativeLayout shareLayout = null;
	private RelativeLayout clearCacheLayout = null;
	private TextView currentVersion;
	private TextView cacheSizeView;

	@Override
	public void initView(View view) {
		feedbackFragment = (RelativeLayout) view
				.findViewById(R.id.feedback_layout);
		helpLayout = (RelativeLayout) view.findViewById(R.id.help_layout);
		aboutFragment = (RelativeLayout) view.findViewById(R.id.about_layout);
		toggleButton = (ToggleButton) view.findViewById(R.id.mespush_toggle);
		logoutLayout = (RelativeLayout) view.findViewById(R.id.logout_layout);
		shareLayout = (RelativeLayout) view.findViewById(R.id.share_layout);
		current_version_layout = (RelativeLayout) view
				.findViewById(R.id.current_version_layout);
		clearCacheLayout = (RelativeLayout) view
				.findViewById(R.id.clear_cache_layout);
		cacheSizeView = (TextView) view.findViewById(R.id.cache_size);
		currentVersion = (TextView) view.findViewById(R.id.current_version);
		currentVersion.setText(MyApplication.getVersionName());

		caculateCacheSize();
	}

	@Override
	public void setListener() {
		feedbackFragment.setOnClickListener(this);
		aboutFragment.setOnClickListener(this);
		helpLayout.setOnClickListener(this);
		toggleButton.setOnCheckedChangeListener(this);
		logoutLayout.setOnClickListener(this);
		current_version_layout.setOnClickListener(this);
		shareLayout.setOnClickListener(this);
		clearCacheLayout.setOnClickListener(this);
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
			startActivity(NavigateActivity.class);
			break;
		case R.id.logout_layout:
			onClickExit();
			break;
		case R.id.current_version_layout:
			checkVersion();
			break;
		case R.id.share_layout:
			startActivity(ShareActivity.class);
			break;
		case R.id.clear_cache_layout:
			onClickCleanCache();
			break;
		default:
			break;
		}
	}

	private void onClickExit() {
		CommonDialog dialog = DialogHelper
				.getPinterestDialogCancelable(getActivity());
		dialog.setTitle("退出登录");
		dialog.setMessage("确定退出登录吗？");
		dialog.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						logout();
						dialog.dismiss();
					}
				});
		dialog.setNegativeButton(R.string.no, null);
		dialog.show();
	}

	/**
	 * 计算缓存的大小
	 */
	private void caculateCacheSize() {
		long fileSize = 0;
		String cacheSize = "0KB";
		File filesDir = getActivity().getFilesDir();
		File cacheDir = getActivity().getCacheDir();

		fileSize += FileUtil.getDirSize(filesDir);
		fileSize += FileUtil.getDirSize(cacheDir);
		// 2.2版本才有将应用缓存转移到sd卡的功能
		if (MyApplication.isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)) {
			File externalCacheDir = getActivity().getExternalCacheDir();
			fileSize += FileUtil.getDirSize(externalCacheDir);
		}
		if (fileSize > 0)
			cacheSize = FileUtil.formatFileSize(fileSize);
		cacheSizeView.setText(cacheSize);
	}

	/**
	 * 清空缓存
	 */
	private void onClickCleanCache() {
		CommonDialog dialog = DialogHelper
				.getPinterestDialogCancelable(getActivity());
		dialog.setTitle("清空缓存？");
		dialog.setMessage("确定清空缓存吗？");
		dialog.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						MyApplication.getInstance().clearAppCache();
						cacheSizeView.setText("0KB");
						dialog.dismiss();
					}
				});
		dialog.setNegativeButton(R.string.no, null);
		dialog.show();
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
			// TODO
		} else {
			// TODO
		}
	}

	// 检查版本
	private void checkVersion() {
		JianFanJiaApiClient.checkVersion(getActivity(),
				new JsonHttpResponseHandler() {
					@Override
					public void onStart() {
						LogTool.d(TAG, "onStart()");
						showWaitDialog("获取新版本");
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						LogTool.d(TAG, "JSONObject response:" + response);
						hideWaitDialog();
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						hideWaitDialog();
						LogTool.d(TAG,
								"Throwable throwable:" + throwable.toString());
						makeTextLong(getString(R.string.tip_login_error_for_network));
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						LogTool.d(TAG, "throwable:" + throwable);
						makeTextLong(getString(R.string.tip_login_error_for_network));
						hideWaitDialog();
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

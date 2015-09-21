package com.jianfanjia.cn.activity;

import java.io.File;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.igexin.sdk.PushManager;
import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.NotifyMessage;
import com.jianfanjia.cn.bean.UpdateVersion;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.http.JianFanJiaApiClient;
import com.jianfanjia.cn.http.LoadClientHelper;
import com.jianfanjia.cn.http.request.LogoutRequest;
import com.jianfanjia.cn.interf.LoadDataListener;
import com.jianfanjia.cn.service.UpdateService;
import com.jianfanjia.cn.tools.FileUtil;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.cn.view.dialog.CommonDialog;
import com.jianfanjia.cn.view.dialog.DialogHelper;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * 
 * @ClassName: SettingActivity
 * @Description: ����
 * @author fengliang
 * @date 2015-9-16 ����9:33:07
 * 
 */
public class SettingActivity extends BaseActivity implements OnClickListener,
		OnCheckedChangeListener {
	private static final String TAG = SettingActivity.class.getName();
	private RelativeLayout feedbackFragment = null;
	private RelativeLayout aboutFragment = null;
	private ToggleButton toggleButton = null;
	private RelativeLayout toggleRelativeLayout = null;
	private RelativeLayout logoutLayout = null;
	private RelativeLayout helpLayout = null;
	private RelativeLayout current_version_layout = null;
	private RelativeLayout shareLayout = null;
	private RelativeLayout clearCacheLayout = null;
	private TextView currentVersion = null;
	private TextView cacheSizeView = null;

	private MainHeadView mainHeadView = null;

	@Override
	public void initView() {
		initMainHeadView();
		feedbackFragment = (RelativeLayout) findViewById(R.id.feedback_layout);
		helpLayout = (RelativeLayout) findViewById(R.id.help_layout);
		aboutFragment = (RelativeLayout) findViewById(R.id.about_layout);
		toggleButton = (ToggleButton) findViewById(R.id.mespush_toggle);
		toggleRelativeLayout = (RelativeLayout) findViewById(R.id.mespush_layout);
		logoutLayout = (RelativeLayout) findViewById(R.id.logout_layout);
		shareLayout = (RelativeLayout) findViewById(R.id.share_layout);
		current_version_layout = (RelativeLayout) findViewById(R.id.current_version_layout);
		clearCacheLayout = (RelativeLayout) findViewById(R.id.clear_cache_layout);
		cacheSizeView = (TextView) findViewById(R.id.cache_size);
		currentVersion = (TextView) findViewById(R.id.current_version);
		currentVersion.setText(MyApplication.getInstance().getVersionName());
		caculateCacheSize();
	}

	private void initMainHeadView() {
		mainHeadView = (MainHeadView) findViewById(R.id.my_setting_head_layout);
		mainHeadView.setBackListener(this);
		mainHeadView
				.setMianTitle(getResources().getString(R.string.my_setting));
		mainHeadView.setLayoutBackground(R.color.head_layout_bg);
		mainHeadView.setDividerVisable(View.VISIBLE);
	}

	@Override
	public void setListener() {
		feedbackFragment.setOnClickListener(this);
		aboutFragment.setOnClickListener(this);
		helpLayout.setOnClickListener(this);
		toggleButton.setOnCheckedChangeListener(this);
		toggleRelativeLayout.setOnClickListener(this);
		logoutLayout.setOnClickListener(this);
		current_version_layout.setOnClickListener(this);
		shareLayout.setOnClickListener(this);
		clearCacheLayout.setOnClickListener(this);
	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean check) {
		LogTool.d(TAG, "check:" + check);
		dataManager.setPushOpen(check);
		if (check) {
			PushManager.getInstance().turnOnPush(SettingActivity.this);
		} else {
			PushManager.getInstance().turnOffPush(SettingActivity.this);
		}
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
			startActivity(HelpActivity.class);
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
		case R.id.head_back_layout:
			finish();
			break;
		case R.id.mespush_layout:
			toggleButton.toggle();
			break;
		default:
			break;
		}
	}

	private void onClickExit() {
		CommonDialog dialog = DialogHelper
				.getPinterestDialogCancelable(SettingActivity.this);
		dialog.setTitle("�˳���¼");
		dialog.setMessage("ȷ���˳���¼��");
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
	 * ���㻺��Ĵ�С
	 */
	private void caculateCacheSize() {
		long fileSize = 0;
		String cacheSize = "0KB";
		File filesDir = getFilesDir();
		File cacheDir = getCacheDir();

		fileSize += FileUtil.getDirSize(filesDir);
		fileSize += FileUtil.getDirSize(cacheDir);
		// 2.2�汾���н�Ӧ�û���ת�Ƶ�sd���Ĺ���
		if (MyApplication.isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)) {
			File externalCacheDir = getExternalCacheDir();
			fileSize += FileUtil.getDirSize(externalCacheDir);
		}
		if (fileSize > 0)
			cacheSize = FileUtil.formatFileSize(fileSize);
		cacheSizeView.setText(cacheSize);
	}

	/**
	 * ��ջ���
	 */
	private void onClickCleanCache() {
		CommonDialog dialog = DialogHelper
				.getPinterestDialogCancelable(SettingActivity.this);
		dialog.setTitle("��ջ��棿");
		dialog.setMessage("ȷ����ջ�����");
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

	/**
	 * ��ȡ���°汾
	 */
	public void showNewVersion(String message, final UpdateVersion updateVersion) {
		CommonDialog dialog = DialogHelper
				.getPinterestDialogCancelable(SettingActivity.this);
		dialog.setTitle("�汾����");
		dialog.setMessage(message);
		dialog.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						startUpdateService(updateVersion.getDownload_url());
						dialog.dismiss();
					}

				});
		dialog.setNegativeButton(R.string.no, null);
		dialog.show();
	}

	private void startUpdateService(String download_url) {
		if (download_url == null)
			return;
		Intent intent = new Intent(this, UpdateService.class);
		intent.putExtra(Constant.DOWNLOAD_URL, download_url);
		startService(intent);
	}

	@Override
	public void onResume() {
		super.onResume();
		LogTool.d(TAG, "---onResume()");
		if (isOpen) {
			toggleButton.setChecked(true);
		} else {
			toggleButton.setChecked(false);
		}
		listenerManeger.addPushMsgReceiveListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		LogTool.d(TAG, "---onPause()");
	}

	@Override
	protected void onStop() {
		super.onStop();
		LogTool.d(TAG, "---onStop()");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		LogTool.d(TAG, "---onDestroy()");
		listenerManeger.removePushMsgReceiveListener(this);
	}

	@Override
	public void onReceiveMsg(NotifyMessage message) {
		LogTool.d(TAG, "message=" + message);
		// sendNotifycation(message);
		showNotify(message);
	}

	// ���汾
	private void checkVersion() {
		JianFanJiaApiClient.checkVersion(SettingActivity.this,
				new JsonHttpResponseHandler() {
					@Override
					public void onStart() {
						LogTool.d(TAG, "onStart()");
						showWaitDialog("����°汾");
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						LogTool.d(TAG, "JSONObject response:" + response);
						hideWaitDialog();
						try {
							if (response.get(Constant.DATA) != null) {
								UpdateVersion updateVersion = (UpdateVersion) JsonParser
										.jsonToBean(response.get(Constant.DATA)
												.toString(),
												UpdateVersion.class);
								if (updateVersion != null) {
									if (Integer.parseInt(updateVersion
											.getVersion_code()) > MyApplication
											.getInstance().getVersionCode()) {
										showNewVersion(
												"���µİ汾�����汾�ţ�"
														+ updateVersion
																.getVersion_name(),
												updateVersion);
									} else {
										makeTextLong("��ǰ�Ѿ������°汾����");
									}
								}
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						hideWaitDialog();
						LogTool.d(TAG,
								"Throwable throwable:" + throwable.toString());
						makeTextLong(getString(R.string.tip_no_internet));
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						LogTool.d(TAG, "throwable:" + throwable);
						makeTextLong(getString(R.string.tip_no_internet));
						hideWaitDialog();
					};
				});
	}

	// �˳���¼
	private void logout() {
		LoadClientHelper.logout(this, new LogoutRequest(this),
				new LoadDataListener() {

					@Override
					public void preLoad() {
						// TODO Auto-generated method stub
						showWaitDialog();
					}

					@Override
					public void loadSuccess() {
						hideWaitDialog();
						makeTextLong("�˳��ɹ�");
						PushManager.getInstance().stopService(
								SettingActivity.this);// ��ȫ��ֹSDK�ķ���
						activityManager.exit();
						startActivity(LoginActivity.class);
					}

					@Override
					public void loadFailture() {
						hideWaitDialog();
						// TODO Auto-generated method stub
						makeTextLong(getString(R.string.tip_no_internet));
					}
				});
	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_setting;
	}

}

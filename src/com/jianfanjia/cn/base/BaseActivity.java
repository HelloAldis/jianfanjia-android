package com.jianfanjia.cn.base;

import java.util.Observable;
import java.util.Observer;
import org.apache.http.Header;
import org.json.JSONObject;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Window;
import android.widget.Toast;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.bean.Message;
import com.jianfanjia.cn.cache.DataManager;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.http.JianFanJiaApiClient;
import com.jianfanjia.cn.inter.manager.ListenerManeger;
import com.jianfanjia.cn.interf.DialogListener;
import com.jianfanjia.cn.interf.PushMsgReceiveListener;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.SharedPrefer;
import com.jianfanjia.cn.tools.UploadManager;
import com.jianfanjia.cn.view.dialog.DialogControl;
import com.jianfanjia.cn.view.dialog.DialogHelper;
import com.jianfanjia.cn.view.dialog.NotifyDialog;
import com.jianfanjia.cn.view.dialog.WaitDialog;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 
 * @ClassName: BaseActivity
 * @Description: activity基类
 * @author fengliang
 * @date 2015年7月24日 上午11:46:40
 * 
 */
public abstract class BaseActivity extends FragmentActivity implements
		DialogControl, PushMsgReceiveListener, Observer {
	protected LayoutInflater inflater = null;
	protected FragmentManager fragmentManager = null;
	protected SharedPrefer sharedPrefer = null;
	protected ImageLoader imageLoader = null;
	protected DisplayImageOptions options = null;
	protected ListenerManeger listenerManeger = null;
	protected UploadManager uploadManager = null;
	private boolean _isVisible;
	private WaitDialog _waitDialog;
	protected DataManager dataManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogTool.d(this.getClass().getName(), "onCreate()");
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(getLayoutId());
		dataManager = DataManager.getInstance();
		init();
		initDao();
		initParams();
		initView();
		setListener();
	}

	public abstract int getLayoutId();

	public abstract void initView();

	public abstract void setListener();

	private void init() {
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		sharedPrefer = new SharedPrefer(this, Constant.SHARED_MAIN);
		fragmentManager = this.getSupportFragmentManager();
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.pix_default)
				.showImageForEmptyUri(R.drawable.pix_default)
				.showImageOnFail(R.drawable.pix_default).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		listenerManeger = ListenerManeger.getListenerManeger();
		uploadManager = UploadManager.getUploadManager(this);
		_isVisible = true;
	}

	private void initParams() {

	}

	private void initDao() {

	}

	@Override
	public void onReceiveMsg(Message message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onStart() {
		super.onStart();
		LogTool.d(this.getClass().getName(), "onStart()");
		listenerManeger.addPushMsgReceiveListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(this.getClass().getName(), "onResume()");
	}

	@Override
	protected void onPause() {
		super.onPause();
		LogTool.d(this.getClass().getName(), "onPause()");
	}

	@Override
	protected void onStop() {
		super.onStop();
		LogTool.d(this.getClass().getName(), "onStop()");
		listenerManeger.removePushMsgReceiveListener(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(this.getClass().getName(), "onDestroy()");
	}

	protected void makeTextShort(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

	protected void makeTextLong(String text) {
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	}

	// 通过Class跳转界面
	protected void startActivity(Class<?> cls) {
		startActivity(cls, null);
	}

	// 含有Bundle通过Class跳转界面
	protected void startActivity(Class<?> cls, Bundle bundle) {
		Intent intent = new Intent();
		intent.setClass(this, cls);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivity(intent);
	}

	// 通过Action跳转界面
	protected void startActivity(String action) {
		startActivity(action, null);
	}

	// 含有Bundle通过Action跳转界面
	protected void startActivity(String action, Bundle bundle) {
		Intent intent = new Intent();
		intent.setAction(action);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivity(intent);
	}

	@Override
	public WaitDialog showWaitDialog() {
		return showWaitDialog(R.string.loading);
	}

	@Override
	public WaitDialog showWaitDialog(int resid) {
		return showWaitDialog(getString(resid));
	}

	@Override
	public WaitDialog showWaitDialog(String message) {
		if (_isVisible) {
			if (_waitDialog == null) {
				_waitDialog = DialogHelper.getWaitDialog(this, message);
			}
			if (_waitDialog != null) {
				_waitDialog.setMessage(message);
				_waitDialog.show();
			}
			return _waitDialog;
		}
		return null;
	}

	@Override
	public void hideWaitDialog() {
		if (_isVisible && _waitDialog != null) {
			try {
				_waitDialog.dismiss();
				_waitDialog = null;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * 消息提醒
	 * 
	 * @param message
	 */
	protected void showNotify(Message message) {
		final NotifyDialog notifyDialog = new NotifyDialog(this,
				R.layout.notify_dialog, message, R.style.progress_dialog);
		notifyDialog.setListener(new DialogListener() {

			@Override
			public void onPositiveButtonClick() {
				notifyDialog.dismiss();
			}

			@Override
			public void onNegativeButtonClick() {
				notifyDialog.dismiss();
			}

		});
		notifyDialog.show();
	}

	// 用户同意改期
	private void agreeReschedule(String processid) {
		JianFanJiaApiClient.agreeReschedule(this, processid,
				new JsonHttpResponseHandler() {
					@Override
					public void onStart() {
						LogTool.d(this.getClass().getName(), "onStart()");
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						LogTool.d(this.getClass().getName(),
								"JSONObject response:" + response);

					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						LogTool.d(this.getClass().getName(),
								"Throwable throwable:" + throwable.toString());
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						LogTool.d(this.getClass().getName(), "throwable:"
								+ throwable);
					};
				});
	}

	// 用户拒绝改期
	private void refuseReschedule(String processid) {
		JianFanJiaApiClient.refuseReschedule(this, processid,
				new JsonHttpResponseHandler() {
					@Override
					public void onStart() {
						LogTool.d(this.getClass().getName(), "onStart()");
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						LogTool.d(this.getClass().getName(),
								"JSONObject response:" + response);

					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						LogTool.d(this.getClass().getName(),
								"Throwable throwable:" + throwable.toString());
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						LogTool.d(this.getClass().getName(), "throwable:"
								+ throwable);
					};
				});
	}

}

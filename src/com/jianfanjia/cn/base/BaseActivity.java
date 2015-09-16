package com.jianfanjia.cn.base;

import java.util.LinkedList;
import org.apache.http.Header;
import org.json.JSONObject;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Toast;
import com.jianfanjia.cn.AppConfig;
import com.jianfanjia.cn.activity.NotifyActivity;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.bean.NotifyMessage;
import com.jianfanjia.cn.bean.ProcessInfo;
import com.jianfanjia.cn.cache.DataManager;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.http.JianFanJiaApiClient;
import com.jianfanjia.cn.inter.manager.ListenerManeger;
import com.jianfanjia.cn.interf.DialogListener;
import com.jianfanjia.cn.interf.LoadDataListener;
import com.jianfanjia.cn.interf.NetStateListener;
import com.jianfanjia.cn.interf.PopWindowCallBack;
import com.jianfanjia.cn.receiver.NetStateReceiver;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.SharedPrefer;
import com.jianfanjia.cn.tools.UploadManager;
import com.jianfanjia.cn.view.AddPhotoPopWindow;
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
		DialogControl, NetStateListener, PopWindowCallBack, LoadDataListener {
	// Activity的集合，将开启的Activity记录于此，退出程序时，逐个关闭Activity
	protected static LinkedList<BaseActivity> queue = new LinkedList<BaseActivity>();
	protected LayoutInflater inflater = null;
	protected FragmentManager fragmentManager = null;
	protected NotificationManager nManager = null;
	protected SharedPrefer sharedPrefer = null;
	protected ImageLoader imageLoader = null;
	protected DisplayImageOptions options = null;
	protected ListenerManeger listenerManeger = null;
	protected UploadManager uploadManager = null;
	protected LocalBroadcastManager localBroadcastManager = null;
	protected NetStateReceiver netStateReceiver = null;
	protected AddPhotoPopWindow popupWindow = null;
	protected static final int NotificationID = 1;
	private boolean _isVisible;
	private WaitDialog _waitDialog;
	protected DataManager dataManager;
	protected ProcessInfo processInfo = null;
	protected AppConfig appConfig;

	protected boolean isOpen = false;

	public static Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			// case Constant.LOAD_SUCCESS:
			// onLoadSuccess();
			// break;
			// case Constant.LOAD_FAILURE:
			// onLoadFailure();
			// break;
			default:
				if (!queue.isEmpty()) {
					queue.getLast().processMessage(msg);
				}
				break;
			}
		};
	};

	public static void sendMessage(Message msg) {
		handler.sendMessage(msg);
	}

	public static void sendEmptyMessage(int what) {
		handler.sendEmptyMessage(what);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogTool.d(this.getClass().getName(), "onCreate()");
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(getLayoutId());
		init();
		initDao();
		initParams();
		initView();
		setListener();
		if (!queue.contains(this)) {
			queue.add(this);
		}
		LogTool.d(this.getClass().getName(),
				"Current Activity number=" + queue.size());
	}

	public abstract int getLayoutId();

	public abstract void initView();

	public abstract void setListener();

	public abstract void processMessage(Message msg);

	private void init() {
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		dataManager = DataManager.getInstance(this, this);
		sharedPrefer = dataManager.sharedPrefer;
		appConfig = AppConfig.getInstance(this);
		fragmentManager = this.getSupportFragmentManager();
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.pix_default)
				.showImageForEmptyUri(R.drawable.pix_default)
				.showImageOnFail(R.drawable.pix_default).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		localBroadcastManager = LocalBroadcastManager.getInstance(this);
		listenerManeger = ListenerManeger.getListenerManeger();
		uploadManager = UploadManager.getUploadManager(this);
		netStateReceiver = new NetStateReceiver(this);
		_isVisible = true;
	}

	private void initParams() {
		processInfo = dataManager.getDefaultProcessInfo();
	}

	private void initDao() {

	}

	@Override
	public void loadSuccess() {
		hideWaitDialog();
	}

	@Override
	public void loadFailture() {
		hideWaitDialog();
	}

	@Override
	public void onConnect() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDisConnect() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onStart() {
		super.onStart();
		LogTool.d(this.getClass().getName(), "onStart()");
	}

	@Override
	protected void onResume() {
		super.onResume();
		LogTool.d(this.getClass().getName(), "onResume()");
		isOpen = sharedPrefer.getValue(Constant.ISOPEN, false);
		Global.isAppBack = false;
		if (queue.contains(this)) {
			queue.remove(this);
			queue.add(this);
		}
		LogTool.d(this.getClass().getName(), "Base  onResume() queue:" + queue);
		registerNetReceiver();
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
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		LogTool.d(this.getClass().getName(), "onDestroy()");
		unregisterNetReceiver();
	}

	public static BaseActivity getActivity(int index) {
		if (index < 0 || index >= queue.size())
			throw new IllegalArgumentException("out of queue");
		return queue.get(index);
	}

	public static BaseActivity getCurrentActivity() {
		return queue.getLast();
	}

	@Override
	public void finish() {
		super.finish();
		if (!queue.isEmpty()) {
			queue.removeLast();
		}
	}

	public static void exit() {// 销毁Activity
		while (queue.size() > 0)
			queue.getLast().finish();
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

	protected void showPopWindow(View view) {
		if (popupWindow == null) {
			popupWindow = new AddPhotoPopWindow(this, this);
		}
		popupWindow.show(view);
	}

	@Override
	public void takecamera() {
		// TODO Auto-generated method stub

	}

	@Override
	public void takePhoto() {
		// TODO Auto-generated method stub

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

	// 注册网络监听广播
	protected void registerNetReceiver() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(netStateReceiver, intentFilter);
	}

	// 取消网络监听广播
	protected void unregisterNetReceiver() {
		unregisterReceiver(netStateReceiver);
	}

	/**
	 * 消息提醒
	 * 
	 * @param message
	 */
	protected void showNotify(NotifyMessage message) {
		final NotifyDialog notifyDialog = new NotifyDialog(this,
				R.layout.notify_dialog, message, R.style.progress_dialog);
		notifyDialog.setListener(new DialogListener() {

			@Override
			public void onPositiveButtonClick() {
				notifyDialog.dismiss();
				// agreeReschedule(processInfo.get_id());
			}

			@Override
			public void onNegativeButtonClick() {
				notifyDialog.dismiss();
				// refuseReschedule(processInfo.get_id());
			}

		});
		notifyDialog.show();
	}

	/**
	 * Notification
	 * 
	 * @param message
	 */
	protected void sendNotifycation(NotifyMessage message) {
		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				this);
		builder.setSmallIcon(R.drawable.ic_launcher);
		String type = message.getType();
		if (type.equals(Constant.CAIGOU_NOTIFY)) {
			builder.setTicker("采购提醒");
			builder.setContentTitle("采购提醒");
		} else if (type.equals(Constant.FUKUAN_NOTIFY)) {
			builder.setTicker("付款提醒");
			builder.setContentTitle("付款提醒");
		} else if (type.equals(Constant.YANQI_NOTIFY)) {
			builder.setTicker("延期提醒");
			builder.setContentTitle("延期提醒");
		}
		builder.setContentText(message.getContent());
		builder.setNumber(0);
		builder.setAutoCancel(true);
		Intent intent = new Intent(this, NotifyActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		builder.setContentIntent(pendingIntent);
		Notification notification = builder.build();
		notification.sound = Uri.parse("android.resource://" + getPackageName()
				+ "/" + R.raw.message);
		nManager.notify(NotificationID, notification);
	}
}

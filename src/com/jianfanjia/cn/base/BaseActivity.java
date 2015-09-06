package com.jianfanjia.cn.base;

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
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.inter.manager.ListenerManeger;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.SharedPrefer;
import com.jianfanjia.cn.view.dialog.DialogControl;
import com.jianfanjia.cn.view.dialog.DialogHelper;
import com.jianfanjia.cn.view.dialog.WaitDialog;
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
		DialogControl {
	protected LayoutInflater inflater = null;
	protected FragmentManager fragmentManager = null;
	protected SharedPrefer sharedPrefer = null;
	protected ImageLoader imageLoader = null;
	protected DisplayImageOptions options = null;
	protected ListenerManeger listenerManeger = null;

	private boolean _isVisible;
	private WaitDialog _waitDialog;

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
				.showImageOnLoading(R.drawable.empty_photo)
				.showImageForEmptyUri(R.drawable.empty_photo)
				.showImageOnFail(R.drawable.empty_photo).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		listenerManeger = ListenerManeger.getListenerManeger();
		_isVisible = true;
	}

	private void initParams() {

	}

	private void initDao() {

	}

	@Override
	protected void onStart() {
		super.onStart();
		LogTool.d(this.getClass().getName(), "onStart()");
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
		overridePendingTransition(R.anim.fragment_list_right_enter, R.anim.fragment_slide_left_exit);
	}

	// 通过Action跳转界面
	protected void startActivity(String action) {
		startActivity(action, null);
		overridePendingTransition(R.anim.fragment_list_right_enter, R.anim.fragment_slide_left_exit);
	}

	// 含有Bundle通过Action跳转界面
	protected void startActivity(String action, Bundle bundle) {
		Intent intent = new Intent();
		intent.setAction(action);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivity(intent);
		overridePendingTransition(R.anim.fragment_list_right_enter, R.anim.fragment_slide_left_exit);
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

}

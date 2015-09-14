package com.jianfanjia.cn.base;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.cache.DataManager;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.inter.manager.ListenerManeger;
import com.jianfanjia.cn.interf.PopWindowCallBack;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.SharedPrefer;
import com.jianfanjia.cn.tools.UploadManager;
import com.jianfanjia.cn.view.AddPhotoPopWindow;
import com.jianfanjia.cn.view.dialog.DialogControl;
import com.jianfanjia.cn.view.dialog.WaitDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @version 1.0
 * @Description Fragment基类
 * @author Administrator
 * @date 2015-8-19 16:02:18
 * 
 */
public abstract class BaseFragment extends Fragment implements OnClickListener,
		PopWindowCallBack {
	protected FragmentManager fragmentManager = null;
	protected DataManager dataManager = null;
	protected LocalBroadcastManager localBroadcastManager = null;
	protected LayoutInflater inflater = null;
	protected SharedPrefer shared = null;
	protected ImageLoader imageLoader = null;
	protected DisplayImageOptions options = null;
	protected ListenerManeger listenerManeger = null;
	protected UploadManager uploadManager = null;
	protected AddPhotoPopWindow popupWindow = null;
	protected String mUserName;// 用户名
	protected String mAccount;// 账号
	protected String mUserImageId;// 头像
	protected String mUserType;// 用户类型
	protected boolean isOpen = false;

	protected Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constant.LOAD_SUCCESS:
				onLoadSuccess();
				break;
			case Constant.LOAD_FAILURE:
				onLoadFailure();
				break;
			default:
				break;
			}

		};
	};

	public void onLoadSuccess() {
		LogTool.d(this.getClass().getName(), "onSuccess");
		hideWaitDialog();
	}

	public void onLoadFailure() {
		LogTool.d(this.getClass().getName(), "onFailure");
		hideWaitDialog();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogTool.d(this.getClass().getName(), "onCreate");
		dataManager = DataManager.getInstance();
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.pix_default)
				.showImageForEmptyUri(R.drawable.pix_default)
				.showImageOnFail(R.drawable.pix_default).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		shared = new SharedPrefer(getActivity(), Constant.SHARED_MAIN);
		listenerManeger = ListenerManeger.getListenerManeger();
		uploadManager = UploadManager.getUploadManager(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LogTool.d(this.getClass().getName(), "onCreateView");
		this.inflater = inflater;
		fragmentManager = getFragmentManager();
		localBroadcastManager = LocalBroadcastManager
				.getInstance(getActivity());
		View view = inflateView(getLayoutId());
		initUserInfo();
		initView(view);
		setListener();
		return view;
	}

	private void initUserInfo() {
		mUserName = DataManager.getInstance().getUserName();
		mAccount = DataManager.getInstance().getAccount();
		mUserImageId = DataManager.getInstance().getUserImagePath();
		mUserType = DataManager.getInstance().getUserType();
		LogTool.d(this.getClass().getName(), "mUserName:" + mUserName
				+ " mAccount:" + mAccount + " userImageId" + mUserImageId);
	}

	@Override
	public void onResume() {
		super.onResume();
		LogTool.d(this.getClass().getName(), "onResume");
		isOpen = shared.getValue(Constant.ISOPEN, false);
	}

	@Override
	public void onPause() {
		super.onPause();
		LogTool.d(this.getClass().getName(), "onPause");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		LogTool.d(this.getClass().getName(), "onDestroy");
	}

	public Context getApplication() {
		return getActivity().getApplication();
	}

	public abstract int getLayoutId();

	public abstract void initView(View view);

	public abstract void setListener();

	protected View inflateView(int resId) {
		return this.inflater.inflate(resId, null);
	}

	@Override
	public void onClick(View v) {

	}

	protected void makeTextShort(String text) {
		Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
	}

	protected void makeTextLong(String text) {
		Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
	}

	// 通过Class跳转界面
	protected void startActivity(Class<?> cls) {
		startActivity(cls, null);
	}

	// 含有Bundle通过Class跳转界面
	protected void startActivity(Class<?> cls, Bundle bundle) {
		Intent intent = new Intent();
		intent.setClass(getActivity(), cls);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivity(intent);
	}

	protected void showPopWindow(View view) {
		if (popupWindow == null) {
			popupWindow = new AddPhotoPopWindow(getActivity(), this);
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

	protected void hideWaitDialog() {
		FragmentActivity activity = getActivity();
		if (activity instanceof DialogControl) {
			((DialogControl) activity).hideWaitDialog();
		}
	}

	protected WaitDialog showWaitDialog(int resid) {
		FragmentActivity activity = getActivity();
		if (activity instanceof DialogControl) {
			return ((DialogControl) activity).showWaitDialog(resid);
		}
		return null;
	}

	protected WaitDialog showWaitDialog() {
		return showWaitDialog(R.string.loading);
	}

	protected WaitDialog showWaitDialog(String str) {
		FragmentActivity activity = getActivity();
		if (activity instanceof DialogControl) {
			return ((DialogControl) activity).showWaitDialog(str);
		}
		return null;
	}

}

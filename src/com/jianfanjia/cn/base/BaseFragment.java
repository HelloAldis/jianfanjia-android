package com.jianfanjia.cn.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.inter.manager.ListenerManeger;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.SharedPrefer;
import com.jianfanjia.cn.view.dialog.DialogControl;
import com.jianfanjia.cn.view.dialog.WaitDialog;

/**
 * @version 1.0
 * @Description Fragment基类
 * @author Administrator
 * @date 2015-8-19 16:02:18
 * 
 */
public abstract class BaseFragment extends Fragment implements OnClickListener {
	protected FragmentManager fragmentManager = null;
	protected LocalBroadcastManager localBroadcastManager = null;
	protected LayoutInflater inflater = null;
	protected SharedPrefer shared = null;
	protected ListenerManeger listenerManeger = null;
	protected boolean isOpen = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogTool.d(this.getClass().getName(), "onCreate");
		shared = new SharedPrefer(getActivity(), Constant.SHARED_MAIN);
		listenerManeger = ListenerManeger.getListenerManeger();
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
		initView(view);
		setListener();
		return view;
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
		// TODO Auto-generated method stub

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
		getActivity().overridePendingTransition(R.anim.fragment_list_right_enter, R.anim.fragment_slide_left_exit);
	}
	
	protected void hideWaitDialog() {
		FragmentActivity activity = getActivity();
		if (activity instanceof DialogControl) {
			((DialogControl) activity).hideWaitDialog();
		}
	}

	protected WaitDialog showWaitDialog() {
		return showWaitDialog(R.string.loading);
	}

	protected WaitDialog showWaitDialog(int resid) {
		FragmentActivity activity = getActivity();
		if (activity instanceof DialogControl) {
			return ((DialogControl) activity).showWaitDialog(resid);
		}
		return null;
	}

	protected WaitDialog showWaitDialog(String str) {
		FragmentActivity activity = getActivity();
		if (activity instanceof DialogControl) {
			return ((DialogControl) activity).showWaitDialog(str);
		}
		return null;
	}

	 

}

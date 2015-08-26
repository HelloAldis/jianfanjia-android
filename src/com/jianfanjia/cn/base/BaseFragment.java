package com.jianfanjia.cn.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Toast;
import com.jianfanjia.cn.bean.RegisterInfo;
import com.jianfanjia.cn.tools.LogTool;

/**
 * @version 1.0
 * @Description Fragment基类
 * @author Administrator
 * @date 2015-8-19 16:02:18
 * 
 */
public abstract class BaseFragment extends Fragment implements OnClickListener {
	protected LayoutInflater inflater = null;
	protected RegisterInfo registerInfo = null;// 注册所需要的信息实体类

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogTool.d(this.getClass().getName(), "onCreate");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LogTool.d(this.getClass().getName(), "onCreateView");
		this.inflater = inflater;
		View view = inflateView(getLayoutId());
		initView(view);
		init();
		setListener();
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		LogTool.d(this.getClass().getName(), "onResume");
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

	private void init() {
		registerInfo = new RegisterInfo();
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
	}

}

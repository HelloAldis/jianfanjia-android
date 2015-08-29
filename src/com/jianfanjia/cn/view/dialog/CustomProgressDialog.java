package com.jianfanjia.cn.view.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;

/**
 * 
 * @ClassName: CustomProgressDialog
 * @Description:自定义ProgressDialog
 * @author fengliang
 * @date 2015-8-28 下午6:39:46
 * 
 */
public class CustomProgressDialog extends ProgressDialog {
	private String title;

	public CustomProgressDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public CustomProgressDialog(Context context, String title, int theme) {
		super(context, theme);
		this.title = title;
		setCanceledOnTouchOutside(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	private void init() {
		setContentView(R.layout.progress_dialog);
		TextView mLoadingTv = (TextView) findViewById(R.id.loadingTv);
		mLoadingTv.setText(title);
	}

}

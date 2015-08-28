package com.jianfanjia.cn.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;
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
	private AnimationDrawable mAnimation;
	private ImageView mImageView;
	private TextView mLoadingTv;
	private String content;
	private int mResid;

	public CustomProgressDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public CustomProgressDialog(Context context, String content, int id,
			int theme) {
		super(context, theme);
		this.content = content;
		this.mResid = mResid;
		setCanceledOnTouchOutside(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	private void init() {
		setContentView(R.layout.progress_dialog);
		mImageView = (ImageView) findViewById(R.id.loadingIv);
		mLoadingTv = (TextView) findViewById(R.id.loadingTv);
		mLoadingTv.setText(content);
		mImageView.setBackgroundResource(mResid);
		// 通过ImageView对象拿到背景显示的AnimationDrawable
		mAnimation = (AnimationDrawable) mImageView.getBackground();
		// 为了防止在onCreate方法中只显示第一帧的解决方案之一
		// mImageView.post(new Runnable() {
		// @Override
		// public void run() {
		// mAnimation.start();
		//
		// }
		// });

	}

}

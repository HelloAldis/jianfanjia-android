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
 * @Description:�Զ���ProgressDialog
 * @author fengliang
 * @date 2015-8-28 ����6:39:46
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
		// ͨ��ImageView�����õ�������ʾ��AnimationDrawable
		mAnimation = (AnimationDrawable) mImageView.getBackground();
		// Ϊ�˷�ֹ��onCreate������ֻ��ʾ��һ֡�Ľ������֮һ
		// mImageView.post(new Runnable() {
		// @Override
		// public void run() {
		// mAnimation.start();
		//
		// }
		// });

	}

}

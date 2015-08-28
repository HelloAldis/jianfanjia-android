package com.jianfanjia.cn.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
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

	public CustomProgressDialog(Context context, String content,
			int theme) {
		super(context, theme);
		this.content = content;
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
	    AlphaAnimation mAlphaAnimation = new AlphaAnimation(0.1f, 1.0f); ////创建一个AlphaAnimation对象，参数从透明到不透明 
		mAlphaAnimation.setDuration(1000);// 设定动画时间
		mAlphaAnimation.setRepeatCount(Animation.INFINITE);//定义动画重复时间
		mAlphaAnimation.setRepeatMode(Animation.REVERSE);//通过设置重复时间定义动画的行为
		mImageView.setAnimation(mAlphaAnimation);
		mAlphaAnimation.start();
	}

}

package com.jianfanjia.cn.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.tools.ImageUtils;

import java.io.IOException;

/**
 * @class ShareActivity
 * @Description 分享
 * @author zhanghao
 * @date 2015-8-27 下午8:22
 * 
 */
public class ShareActivity extends BaseActivity implements OnClickListener,
		OnLongClickListener {
	private static final String TAG = ShareActivity.class.getName();
	private TextView backView;// 返回视图
	private ImageView mIvCode;// 当前版本

	@Override
	public void initView() {
		backView = (TextView) findViewById(R.id.share_back);
		mIvCode = (ImageView) findViewById(R.id.share_qr);
	}

	@Override
	public void setListener() {
		backView.setOnClickListener(this);
		mIvCode.setOnLongClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.share_back:
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onLongClick(View v) {
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.icon_jianfanjia_qr);
		try {
			ImageUtils.saveImageToSD(ShareActivity.this, Constant.IMAG_PATH
					+ "myqr.jpg", bitmap, 100);
			makeTextLong(getResources().getString(R.string.save_image_success));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			makeTextLong(getResources().getString(R.string.save_image_failure));
		}
		return true;
	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_share;
	}

}
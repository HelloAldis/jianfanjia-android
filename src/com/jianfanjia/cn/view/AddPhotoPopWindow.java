package com.jianfanjia.cn.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.interf.PopWindowCallBack;

public class AddPhotoPopWindow extends PopupWindow implements OnClickListener {
	private PopWindowCallBack callback;
	private Button open_camera;
	private Button open_album;
	private Button cancel;
	private LayoutInflater inflater;
	private View menuView;

	public AddPhotoPopWindow(Context context, PopWindowCallBack callback) {
		super(context);
		this.callback = callback;
		inflater = LayoutInflater.from(context);
		menuView = inflater.inflate(R.layout.popwin_dialog, null);
		open_camera = (Button) menuView.findViewById(R.id.btn_open_camera);
		open_album = (Button) menuView.findViewById(R.id.btn_open_album);
		cancel = (Button) menuView.findViewById(R.id.btn_cancel);
		open_camera.setOnClickListener(this);
		open_album.setOnClickListener(this);
		cancel.setOnClickListener(this);
		// 设置SelectPicPopupWindow的View
		this.setContentView(menuView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.popub_anim);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_open_camera:
			dismiss();
			callback.takecamera();
			break;
		case R.id.btn_open_album:
			dismiss();
			callback.takePhoto();
			break;
		case R.id.btn_cancel:
			dismiss();
			break;
		default:
			break;
		}

	}

}

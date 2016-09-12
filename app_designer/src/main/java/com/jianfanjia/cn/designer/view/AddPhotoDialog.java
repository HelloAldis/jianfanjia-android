package com.jianfanjia.cn.designer.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.ui.interf.PopWindowCallBack;

public class AddPhotoDialog extends Dialog implements OnClickListener {

	private PopWindowCallBack callback = null;
	private Button open_camera = null;
	private Button open_album = null;
	private Button cancel = null;
	private LayoutInflater inflater = null;
	private View menuView = null;
	private WindowManager.LayoutParams lp = null;
	private Window window = null;

	public AddPhotoDialog(Context context, PopWindowCallBack callback) {
		super(context, R.style.Theme_Light_Dialog);

		window = getWindow();
		window.getDecorView().setPadding(0, 0, 0, 0);
		lp = window.getAttributes();
		//设置窗口宽度为充满全屏
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		//设置窗口高度为包裹内容
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		window.setAttributes(lp);
		window.setWindowAnimations(R.style.popub_anim);
		window.setGravity(Gravity.BOTTOM);

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
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_open_camera:
				dismiss();
				callback.firstItemClick();
				break;
			case R.id.btn_open_album:
				dismiss();
				callback.secondItemClick();
				break;
			case R.id.btn_cancel:
				dismiss();
				break;
			default:
				break;
		}
	}

}

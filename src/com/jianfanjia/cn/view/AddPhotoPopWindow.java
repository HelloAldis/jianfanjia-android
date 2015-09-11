package com.jianfanjia.cn.view;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
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
	private LinearLayout buttonLayout;
	private Activity activity;
	private WindowManager.LayoutParams lp;
	private Window window;

	public AddPhotoPopWindow(Activity activity, PopWindowCallBack callback) {
		super(activity);
		this.callback = callback;
		inflater = LayoutInflater.from(activity);
		menuView = inflater.inflate(R.layout.popwin_dialog, null);
		open_camera = (Button) menuView.findViewById(R.id.btn_open_camera);
		open_album = (Button) menuView.findViewById(R.id.btn_open_album);
		cancel = (Button) menuView.findViewById(R.id.btn_cancel);
		open_camera.setOnClickListener(this);
		open_album.setOnClickListener(this);
		cancel.setOnClickListener(this);
		// ����SelectPicPopupWindow��View
		this.setContentView(menuView);
		// ����SelectPicPopupWindow��������Ŀ�
		this.setWidth(LayoutParams.MATCH_PARENT);
		// ����SelectPicPopupWindow��������ĸ�
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// ����SelectPicPopupWindow��������ɵ��
		this.setFocusable(true);
		// ����SelectPicPopupWindow�������嶯��Ч��
		this.setAnimationStyle(R.style.popub_anim);
		// ʵ����һ��ColorDrawable��ɫΪ��͸��
		ColorDrawable dw = new ColorDrawable(0x80000000);
		// ����SelectPicPopupWindow��������ı���
		this.setBackgroundDrawable(dw);
		// ���������䰵Ч��
		lp = activity.getWindow().getAttributes();
		window = activity.getWindow();
		this.setOutsideTouchable(true);
		this.setFocusable(true);
		this.setOnDismissListener(new OnDismissListener() {

			// ��dismiss�лָ�͸����
			@Override
			public void onDismiss() {
				lp.alpha = 1f;
				window.setAttributes(lp);
			}
		});
	}
	
	//���ð�͸��
	private void setShade(){
		lp.alpha = 0.4f;
		window.setAttributes(lp);
	}

	public void show(View view) {
		setShade();
		showAtLocation(view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
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

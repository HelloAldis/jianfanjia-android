package com.jianfanjia.cn.designer.view;

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
import android.widget.PopupWindow;

import com.jianfanjia.cn.designer.R;

public class DeletePicPopWindow extends PopupWindow implements OnClickListener {
	private Button secondButton = null;
	private Button cancel = null;
	private LayoutInflater inflater = null;
	private View menuView = null;
	private Activity activity = null;
	private WindowManager.LayoutParams lp = null;
	private Window window = null;

	public DeletePicPopWindow(Activity activity,OnClickListener listener) {
		super(activity);
		inflater = LayoutInflater.from(activity);
		menuView = inflater.inflate(R.layout.dialog_delete_pic, null);
		secondButton = (Button) menuView.findViewById(R.id.dialog_btn_delete_pic);
		cancel = (Button) menuView.findViewById(R.id.btn_cancel);
		secondButton.setOnClickListener(listener);
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
		ColorDrawable dw = new ColorDrawable(0x80000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		// 产生背景变暗效果
		lp = activity.getWindow().getAttributes();
		window = activity.getWindow();
		this.setOutsideTouchable(true);
		this.setFocusable(true);
		this.setOnDismissListener(new OnDismissListener() {

			// 在dismiss中恢复透明度
			@Override
			public void onDismiss() {
				lp.alpha = 1f;
				window.setAttributes(lp);
			}
		});
	}

	// 设置半透明
	private void setShade() {
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
		case R.id.btn_cancel:
			dismiss();
			break;
		default:
			break;
		}
	}

}

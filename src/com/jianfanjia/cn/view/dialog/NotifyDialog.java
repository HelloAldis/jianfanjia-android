package com.jianfanjia.cn.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.interf.ConfirmListener;

/**
 * 
 * @ClassName: NotifyDialog
 * @Description:提醒对话框
 * @author fengliang
 * @date 2015-8-29 上午10:17:02
 * 
 */
public class NotifyDialog extends Dialog implements
		android.view.View.OnClickListener {
	private ConfirmListener listener;
	private String title;
	private String content;

	public NotifyDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public NotifyDialog(Context context, String title, String content, int theme) {
		super(context, theme);
		this.title = title;
		this.content = content;
		init();
	}

	private void init() {
		setContentView(R.layout.notify_dialog);
		TextView titleTv = (TextView) findViewById(R.id.titleTv);
		TextView contentTv = (TextView) findViewById(R.id.contentTv);
		Button confirm = (Button) findViewById(R.id.btn_confirm);
		confirm.setOnClickListener(this);
		titleTv.setText(title);
		contentTv.setText(content);
	}

	@Override
	public void onClick(View V) {
		listener.confirm();
	}

	public void setListener(ConfirmListener listener) {
		this.listener = listener;
	}

}

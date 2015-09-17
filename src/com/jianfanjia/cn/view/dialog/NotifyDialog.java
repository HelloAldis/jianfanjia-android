package com.jianfanjia.cn.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.bean.NotifyMessage;
import com.jianfanjia.cn.interf.DialogListener;

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
	private DialogListener listener;
	private NotifyMessage message;

	public NotifyDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public NotifyDialog(Context context, NotifyMessage message, int theme,
			DialogListener listener) {
		super(context, theme);
		this.message = message;
		this.listener = listener;
		init();
	}

	private void init() {
		setContentView(R.layout.notify_dialog);
		TextView titleTv = (TextView) findViewById(R.id.titleTv);
		TextView contentTv = (TextView) findViewById(R.id.contentTv);
		Button agree = (Button) findViewById(R.id.btn_agree);
		Button refuse = (Button) findViewById(R.id.btn_refuse);
		agree.setOnClickListener(this);
		refuse.setOnClickListener(this);
		String type = message.getType();
		if (type.equals("0")) {
			titleTv.setText("延期提醒");
		} else if (type.equals("1")) {
			titleTv.setText("采购提醒");
		} else if (type.equals("2")) {
			titleTv.setText("付款提醒");
		}
		contentTv.setText(message.getContent());
	}

	@Override
	public void onClick(View V) {
		switch (V.getId()) {
		case R.id.btn_agree:
			dismiss();
			listener.onPositiveButtonClick();
			break;
		case R.id.btn_refuse:
			dismiss();
			listener.onNegativeButtonClick();
			break;
		}
	}

}

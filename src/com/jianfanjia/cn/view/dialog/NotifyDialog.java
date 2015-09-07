package com.jianfanjia.cn.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.bean.Message;
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
	private int layoutId;
	private Message message;

	public NotifyDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public NotifyDialog(Context context, int layoutId, Message message,
			int theme) {
		super(context, theme);
		this.layoutId = layoutId;
		this.message = message;
		init();
	}

	private void init() {
		setContentView(layoutId);
		TextView titleTv = (TextView) findViewById(R.id.titleTv);
		TextView contentTv = (TextView) findViewById(R.id.contentTv);
		Button confirm = (Button) findViewById(R.id.btn_confirm);
		confirm.setOnClickListener(this);
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
		listener.confirm();
	}

	public void setListener(ConfirmListener listener) {
		this.listener = listener;
	}

}

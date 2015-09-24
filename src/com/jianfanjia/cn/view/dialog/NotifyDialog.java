package com.jianfanjia.cn.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.bean.NotifyMessage;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.interf.DialogListener;

/**
 * 
 * @ClassName: NotifyDialog
 * @Description:���ѶԻ���
 * @author fengliang
 * @date 2015-8-29 ����10:17:02
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
		init(context);
	}

	private void init(Context context) {
		setContentView(R.layout.notify_dialog);
		TextView titleTv = (TextView) findViewById(R.id.titleTv);
		TextView contentTv = (TextView) findViewById(R.id.contentTv);
		Button ok = (Button) findViewById(R.id.btn_ok);
		Button agree = (Button) findViewById(R.id.btn_agree);
		Button refuse = (Button) findViewById(R.id.btn_refuse);
		ok.setOnClickListener(this);
		agree.setOnClickListener(this);
		refuse.setOnClickListener(this);
		String type = message.getType();
		if (type.equals(Constant.CAIGOU_NOTIFY)) {
			titleTv.setText(context.getResources().getString(
					R.string.caigouText));
			ok.setVisibility(View.VISIBLE);
			agree.setVisibility(View.GONE);
			refuse.setVisibility(View.GONE);
		} else if (type.equals(Constant.FUKUAN_NOTIFY)) {
			titleTv.setText(context.getResources().getString(
					R.string.fukuanText));
			ok.setVisibility(View.VISIBLE);
			agree.setVisibility(View.GONE);
			refuse.setVisibility(View.GONE);
		} else if (type.equals(Constant.YANQI_NOTIFY)) {
			titleTv.setText(context.getResources()
					.getString(R.string.yanqiText));
			ok.setVisibility(View.GONE);
			agree.setVisibility(View.VISIBLE);
			refuse.setVisibility(View.VISIBLE);
		} else {
			titleTv.setText(context.getResources().getString(
					R.string.yanshouText));
			ok.setVisibility(View.VISIBLE);
			agree.setVisibility(View.GONE);
			refuse.setVisibility(View.GONE);
		}
		contentTv.setText(message.getContent());
	}

	@Override
	public void onClick(View V) {
		switch (V.getId()) {
		case R.id.btn_ok:
			dismiss();
			listener.onConfirmButtonClick();
			break;
		case R.id.btn_agree:
			dismiss();
			listener.onPositiveButtonClick();
			break;
		case R.id.btn_refuse:
			dismiss();
			listener.onNegativeButtonClick();
			break;
		default:
			break;
		}
	}

}

package com.jianfanjia.cn.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.interf.DialogListener;

/**
 * 
 * @ClassName: HintDialog
 * @Description: 提示对话框
 * @author fengliang
 * @date 2015-9-5 上午10:50:35
 * 
 */
public class HintDialog extends Dialog implements
		android.view.View.OnClickListener {
	private DialogListener listener;
	private String title;
	private String content;
	private int layoutId;

	public HintDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public HintDialog(Context context, int layoutId, String title,
			String content, int theme) {
		super(context, theme);
		this.layoutId = layoutId;
		this.title = title;
		this.content = content;
		init();
	}

	private void init() {// R.layout.hint_dialog
		setContentView(layoutId);
		TextView titleTv = (TextView) findViewById(R.id.titleTv);
		TextView contentTv = (TextView) findViewById(R.id.contentTv);
		Button ok = (Button) findViewById(R.id.btn_ok);
		Button no = (Button) findViewById(R.id.btn_no);
		ok.setOnClickListener(this);
		no.setOnClickListener(this);
		titleTv.setText(title);
		contentTv.setText(content);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_ok:
			listener.onPositiveButtonClick();
			break;
		case R.id.btn_no:
			listener.onNegativeButtonClick();
			break;
		default:
			break;
		}
	}

	public void setListener(DialogListener listener) {
		this.listener = listener;
	}

}

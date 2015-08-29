package com.jianfanjia.cn.view.dialog;

import android.content.Context;
import android.os.Bundle;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.view.MyDialog;

public class DatePickerDialog extends MyDialog {

	public DatePickerDialog(Context context) {
		super(context);
	}

	public DatePickerDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.date_pick_dialog);
		initView();
	}

	private void initView() {

	}

}

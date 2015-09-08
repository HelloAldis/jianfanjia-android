package com.jianfanjia.cn.activity;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jianfanjia.cn.base.BaseActivity;

public class EditInfoActivity extends BaseActivity implements OnClickListener{
	
	private TextView backView;
	private EditText editInfoView;
	private Button confirmView;

	@Override
	public int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.activity_edit_info;
	}

	@Override
	public void initView() {
		backView = (TextView) findViewById(R.id.edit_back);
		editInfoView = (EditText) findViewById(R.id.edit_info);
		confirmView = (Button) findViewById(R.id.edit_info);
		
		
	}

	@Override
	public void setListener() {
		confirmView.setOnClickListener(this);
		backView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int viewId = v.getId();
		switch (viewId) {
		case R.id.edit_back:
			finish();
			break;

		default:
			break;
		}
		
	}
	
}

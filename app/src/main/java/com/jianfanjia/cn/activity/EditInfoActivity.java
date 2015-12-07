package com.jianfanjia.cn.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.view.MainHeadView;

public class EditInfoActivity extends BaseActivity implements OnClickListener {
	private static final String TAG = EditInfoActivity.class.getName();
	private MainHeadView mainHeadView;
	private EditText editInfoView;
	private Button confirmView;
	private Intent intent;
	private int type;// 输入类型
	private String content;//输入内容
	InputFilter[] namefilters = { new InputFilter.LengthFilter(20) };
	InputFilter[] addressfilters = { new InputFilter.LengthFilter(100) };

	@Override
	public void initView() {
		initMainView();
		editInfoView = (EditText) findViewById(R.id.edit_info);
		editInfoView.addTextChangedListener(textWatcher);
		confirmView = (Button) findViewById(R.id.btn_commit);
		confirmView.setEnabled(false);

		intent = getIntent();
		type = intent.getIntExtra(Constant.EDIT_TYPE, 0);
		content = intent.getStringExtra(Constant.EDIT_CONTENT);
		if(!TextUtils.isEmpty(content)) {
			editInfoView.setText(content);
			editInfoView.setSelection(content.length());
		}
		if (type == Constant.REQUESTCODE_EDIT_USERNAME) {
			editInfoView.setHint(R.string.input_name);
			editInfoView.setFilters(namefilters);
			mainHeadView.setMianTitle(getString(R.string.user_name));
		} else {
			editInfoView.setHint(R.string.input_home);
			editInfoView.setFilters(addressfilters);
			mainHeadView.setMianTitle(getString(R.string.user_home));
		}
	}

	private void initMainView() {
		mainHeadView = (MainHeadView) findViewById(R.id.edit_head_layout);
		mainHeadView.setBackListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
//		editInfoView.requestFocus();
	}

	@Override
	public void setListener() {
		confirmView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.head_back_layout:
			finish();
			break;
		case R.id.btn_commit:
			String content = editInfoView.getEditableText().toString().trim();
			if (!TextUtils.isEmpty(content)) {
				intent.putExtra(Constant.EDIT_CONTENT, content);
				setResult(RESULT_OK, intent);
				finish();
			}
			break;
		default:
			break;
		}
	}

	private TextWatcher textWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void afterTextChanged(Editable s) {
			if (!TextUtils.isEmpty(s.toString().trim())) {
				confirmView.setEnabled(true);
			}else{
				confirmView.setEnabled(false);
			}
		}
	};

	@Override
	public int getLayoutId() {
		return R.layout.activity_edit_info;
	}

}
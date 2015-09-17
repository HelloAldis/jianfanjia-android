package com.jianfanjia.cn.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.config.Constant;

public class EditInfoActivity extends BaseActivity implements OnClickListener {
	private static final String TAG = EditInfoActivity.class.getName();
	private TextView backView;
	private EditText editInfoView;
	private Button confirmView;
	private Intent intent;
	private int type;//  ‰»Î¿‡–Õ

	@Override
	public void initView() {
		backView = (TextView) findViewById(R.id.edit_back);
		editInfoView = (EditText) findViewById(R.id.edit_info);
		editInfoView.addTextChangedListener(textWatcher);
		confirmView = (Button) findViewById(R.id.btn_commit);
		confirmView.setEnabled(false);

		intent = getIntent();
		type = intent.getIntExtra(Constant.EDIT_TYPE, 0);
		if (type == Constant.REQUESTCODE_EDIT_USERNAME) {
			editInfoView.setHint(R.string.input_name);
		} else {
			editInfoView.setHint(R.string.input_home);
		}

	}

	@Override
	public void setListener() {
		confirmView.setOnClickListener(this);
		backView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.edit_back:
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
			}
		}
	};

	@Override
	public int getLayoutId() {
		return R.layout.activity_edit_info;
	}

}

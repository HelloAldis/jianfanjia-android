package com.jianfanjia.cn.activity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.Message;
import com.jianfanjia.cn.tools.LogTool;

/**
 * @class FeedBackActivity
 * @Description 用户反馈
 * @author zhanghao
 * @date 2015-8-27 10:11
 * 
 */
public class FeedBackActivity extends BaseActivity implements OnClickListener {
	private static final String TAG = FeedBackActivity.class.getName();
	private TextView backView;// 返回视图
	private EditText feedContentView;//
	private Button confirm;

	private TextWatcher textWatcher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			String content = s.toString().trim();
			if(TextUtils.isEmpty(content)){
				confirm.setEnabled(false);
			}else{
				confirm.setEnabled(true);
			}
		}
	};
	
	@Override
	public void initView() {
		backView = (TextView) findViewById(R.id.feedback_back);
		feedContentView = (EditText) findViewById(R.id.add_feedback);
		feedContentView.addTextChangedListener(textWatcher);
		confirm = (Button) findViewById(R.id.btn_commit);
		confirm.setEnabled(false);
	}

	@Override
	public void setListener() {
		backView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.feedback_back:
			finish();
			break;
		case R.id.btn_commit:
			
		default:
			break;
		}
	}

	@Override
	public void onReceiveMsg(Message message) {
		LogTool.d(TAG, "message=" + message);
		showNotify(message);
	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_feedback;
	}

}

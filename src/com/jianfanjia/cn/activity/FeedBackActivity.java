package com.jianfanjia.cn.activity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.Message;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.http.JianFanJiaApiClient;
import com.jianfanjia.cn.tools.LogTool;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * @class FeedBackActivity
 * @Description 用户反馈
 * @author zhanghao
 * @date 2015-8-27 10:11
 * 
 */
public class FeedBackActivity extends BaseActivity implements OnClickListener {
	private static final String TAG = FeedBackActivity.class.getName();
	private TextView backView = null;// 返回视图
	private EditText feedContentView = null;
	private Button confirm = null;

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
		confirm.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.feedback_back:
			finish();
			break;
		case R.id.btn_commit:
			String content = feedContentView.getText().toString().trim();
			feedBack(content, "0");
		default:
			break;
		}
	}

	private void feedBack(String content, String platform) {
		JianFanJiaApiClient.feedBack(FeedBackActivity.this, content, platform,
				new JsonHttpResponseHandler() {
					@Override
					public void onStart() {
						LogTool.d(TAG, "onStart()");
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						LogTool.d(TAG, "JSONObject response:" + response);
						try {
							if (response.has(Constant.SUCCESS_MSG)) {
								makeTextLong(response.get(Constant.SUCCESS_MSG)
										.toString());
								finish();
							} else if (response.has(Constant.ERROR_MSG)) {
								makeTextLong(response.get(Constant.ERROR_MSG)
										.toString());
							}
						} catch (JSONException e) {
							e.printStackTrace();
							makeTextLong(getString(R.string.tip_login_error_for_network));
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						LogTool.d(TAG, "Throwable throwable:" + throwable);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						LogTool.d(TAG, "statusCode:" + statusCode
								+ " throwable:" + throwable);
					};
				});
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
			String content = s.toString().trim();
			if (TextUtils.isEmpty(content)) {
				confirm.setEnabled(false);
			} else {
				confirm.setEnabled(true);
			}
		}
	};

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

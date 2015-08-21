package com.jianfanjia.cn.fragment;

import org.apache.http.Header;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.RegisterActivity;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.http.JianFanJiaApiClient;
import com.jianfanjia.cn.tools.TDevice;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * @version 1.0
 * @Description 输入用户名密码的Fragment
 * @author zhanghao
 * @date 2015-8-21 上午9:15
 *
 */
public class RegInputPhoneFragment extends BaseFragment{
	
	private EditText mUserName;//用户名输入框
	private EditText mPassword;//密码输入框
	private Button nextView;//下一步
	private TextView backView;//返回
	
	private ImageView indicatorView;//指示器
	private TextView proTipView;//提示操作
	
	private RegisterActivity registerActivity;
	
	public RegInputPhoneFragment(RegisterActivity registerActivity){
		this.registerActivity = registerActivity;
	}
	
	@Override
	public int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_register_input_phone;
	}

	@Override
	public void initView(View view) {
		nextView = (Button)view.findViewById(R.id.btn_next);
		backView = (TextView)view.findViewById(R.id.goback);
		mUserName = (EditText)view.findViewById(R.id.et_username);
		mPassword = (EditText)view.findViewById(R.id.et_password);
		
		indicatorView = (ImageView)view.findViewById(R.id.indicator);
		indicatorView.setImageResource(R.drawable.rounded_enrollment2);
		
		proTipView = (TextView)view.findViewById(R.id.register_pro);
		proTipView.setText(getString(R.string.input_phone));
	}
	
	@Override
	public void onClick(View arg0) {
		super.onClick(arg0);
		int viewId = arg0.getId();
		switch (viewId) {
		case R.id.btn_next:
			if(checkInput()){
				//发送验证码
				JianFanJiaApiClient.send_verification(mUserName.getEditableText().toString(), asyncHttpResponseHandler);
				
				//保存用户名和密码
				registerActivity.getRegisterInfo().setPhone(mUserName.getEditableText().toString());
				registerActivity.getRegisterInfo().setPass(mPassword.getEditableText().toString());
				
				//进入下一步页面
				registerActivity.next();
			}
			break;
		case R.id.goback:
			registerActivity.back();
			break;
		default:
			break;
		}
	}
	
	private JsonHttpResponseHandler asyncHttpResponseHandler = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				org.json.JSONObject response) {
			makeTextLong(response.toString());
		}
		
		public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
			
		};
	};
	
	private boolean checkInput() {
		if (!TDevice.hasInternet()) {
			makeTextShort(getResources().getString(R.string.tip_no_internet));
			return false;
		}

		String uName = mUserName.getText().toString();
		if (TextUtils.isEmpty(uName)) {
			makeTextShort(getResources().getString(
					R.string.tip_please_input_username));
			mUserName.requestFocus();
			return false;
		}
		String pwd = mPassword.getText().toString();
		if (TextUtils.isEmpty(pwd)) {
			makeTextShort(getResources().getString(
					R.string.tip_please_input_password));
			mPassword.requestFocus();
			return false;
		}
		return true;
	}

	@Override
	public void setListener() {
		nextView.setOnClickListener(this);
		backView.setOnClickListener(this);
	}

}

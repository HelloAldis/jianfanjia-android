package com.jianfanjia.cn.fragment;

import org.apache.http.Header;

import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
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
 * @Description 注册选择角色Fragment
 * @author zhanghao
 * @date 2015-8-21 上午9:15
 *
 */
public class ReginputVerificationFragment extends BaseFragment{
	
	private Button nextView;//下一步
	private TextView backView;//返回
	private EditText mEdVerif;//验证码输入框
	
	private ImageView indicatorView;//指示器
	private TextView proTipView;//提示操作
	
	private RegisterActivity registerActivity;
	
	public ReginputVerificationFragment(RegisterActivity registerActivity){
		this.registerActivity = registerActivity;
	}

	@Override
	public int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_register_input_verification;
	}

	@Override
	public void initView(View view) {
		nextView = (Button)view.findViewById(R.id.btn_commit);
		backView = (TextView)view.findViewById(R.id.goback);
		mEdVerif = (EditText)view.findViewById(R.id.et_verification);
		
		indicatorView = (ImageView)view.findViewById(R.id.indicator);
		indicatorView.setImageResource(R.drawable.rounded_enrollment3);
		
		proTipView = (TextView)view.findViewById(R.id.register_pro);
		proTipView.setText(getString(R.string.verification_code_sended));
	}
	
	@Override
	public void onClick(View arg0) {
		super.onClick(arg0);
		int viewId = arg0.getId();
		switch (viewId) {
		case R.id.btn_next:
			if(checkInput()){
				registerActivity.getRegisterInfo().setCode(mEdVerif.getEditableText().toString());
				
				JianFanJiaApiClient.register(registerActivity.getRegisterInfo(), asyncHttpResponseHandler);
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
	
	public boolean checkInput(){
		if (!TDevice.hasInternet()) {
			makeTextShort(getResources().getString(R.string.tip_no_internet));
			return false;
		}

		String verif = mEdVerif.getText().toString();
		if (TextUtils.isEmpty(verif )) {
			makeTextShort(getResources().getString(
					R.string.hint_verification));
			mEdVerif.requestFocus();
			return false;
		}
		return true;
	}

	@Override
	public void setListener() {
		nextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(checkInput()){
					Log.i(this.getClass().getName(), "register");
					
					registerActivity.getRegisterInfo().setCode(mEdVerif.getEditableText().toString());
					
					JianFanJiaApiClient.register(registerActivity.getRegisterInfo(), asyncHttpResponseHandler);
				}
			}
		});
		backView.setOnClickListener(this);
	}

}

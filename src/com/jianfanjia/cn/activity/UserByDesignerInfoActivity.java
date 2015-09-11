package com.jianfanjia.cn.activity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.Message;
import com.jianfanjia.cn.bean.UserByDesignerInfo;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.http.JianFanJiaApiClient;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * 
 * @ClassName: UserByDesignerInfoActivity
 * @Description:用户个人信息(设计师)
 * @author fengliang
 * @date 2015-9-5 下午2:01:42
 * 
 */
public class UserByDesignerInfoActivity extends BaseActivity implements
		OnClickListener {
	private static final String TAG = UserByDesignerInfoActivity.class
			.getName();
	private TextView ownerinfo_back = null;
	private RelativeLayout headLayout = null;
	private TextView nameText = null;
	private TextView sexText = null;
	private TextView phoneText = null;
	private TextView addressText = null;
	private TextView homeText = null;
	private Button btn_confirm = null;
	private RelativeLayout userNameRelativeLayout = null;
	private RelativeLayout homeRelativeLayout = null;

	@Override
	public void initView() {
		ownerinfo_back = (TextView) this.findViewById(R.id.ownerinfo_back);
		headLayout = (RelativeLayout) this.findViewById(R.id.head_layout);
		nameText = (TextView) this.findViewById(R.id.nameText);
		sexText = (TextView) this.findViewById(R.id.sexText);
		phoneText = (TextView) this.findViewById(R.id.phoneText);
		addressText = (TextView) this.findViewById(R.id.addressText);
		homeText = (TextView) this.findViewById(R.id.homeText);
		btn_confirm = (Button) this.findViewById(R.id.btn_confirm);

		userNameRelativeLayout = (RelativeLayout) this
				.findViewById(R.id.name_layout);
		homeRelativeLayout = (RelativeLayout) this
				.findViewById(R.id.home_layout);
		get_Designer_Info();
	}

	@Override
	public void setListener() {
		ownerinfo_back.setOnClickListener(this);
		headLayout.setOnClickListener(this);
		btn_confirm.setOnClickListener(this);
		userNameRelativeLayout.setOnClickListener(this);
		homeRelativeLayout.setOnClickListener(this);
	}

	private void get_Designer_Info() {
		JianFanJiaApiClient.get_Designer_Info(UserByDesignerInfoActivity.this,
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
							if (response.has(Constant.DATA)) {
								UserByDesignerInfo info = JsonParser
										.jsonToBean(response.get(Constant.DATA)
												.toString(),
												UserByDesignerInfo.class);
								if (null != info) {
									if (!TextUtils.isEmpty(info.getUsername())) {
										nameText.setText(info.getUsername());
									} else {
										nameText.setText("设计师");
									}
									String sexInfo = info.getSex();
									if (!TextUtils.isEmpty(sexInfo)) {
										if (sexInfo.equals("1")) {
											sexText.setText("男");
										} else {
											sexText.setText("女");
										}
									} else {
										sexText.setText("无");
									}
									if (!TextUtils.isEmpty(info.getPhone())) {
										phoneText.setText(info.getPhone());
									} else {
										phoneText.setText("无");
									}
									if (!TextUtils.isEmpty(info.getDistrict())) {
										addressText.setText(info.getDistrict());
									} else {
										addressText.setText("无");
									}
									if (!TextUtils.isEmpty(info.getAddress())) {
										homeText.setText(info.getAddress());
									} else {
										homeText.setText("无");
									}
								}
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
						LogTool.d(TAG,
								"Throwable throwable:" + throwable.toString());
						makeTextLong(getString(R.string.tip_login_error_for_network));
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						LogTool.d(TAG, "throwable:" + throwable);
						makeTextLong(getString(R.string.tip_login_error_for_network));
					};
				});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ownerinfo_back:
			finish();
			break;
		case R.id.head_layout:
			break;
		case R.id.btn_confirm:
			break;
		case R.id.name_layout:
			Intent name = new Intent(UserByDesignerInfoActivity.this,
					EditInfoActivity.class);
			name.putExtra(Constant.EDIT_TYPE,
					Constant.REQUESTCODE_EDIT_USERNAME);
			startActivityForResult(name, Constant.REQUESTCODE_EDIT_USERNAME);
			break;
		case R.id.address_layout:
			Intent address = new Intent(UserByDesignerInfoActivity.this,
					EditInfoActivity.class);
			address.putExtra(Constant.EDIT_TYPE,
					Constant.REQUESTCODE_EDIT_ADDRESS);
			startActivityForResult(address, Constant.REQUESTCODE_EDIT_ADDRESS);
			break;
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
		return R.layout.activity_designer_info;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			String content = data.getStringExtra(Constant.EDIT_CONTENT);
			if (!TextUtils.isEmpty(content)) {
				if (requestCode == Constant.REQUESTCODE_EDIT_USERNAME) {
					nameText.setText(content);
				} else if (requestCode == Constant.REQUESTCODE_EDIT_ADDRESS) {
					homeText.setText(content);
				}
			}
		}
	}

}

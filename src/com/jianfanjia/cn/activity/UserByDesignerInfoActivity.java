package com.jianfanjia.cn.activity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import com.jianfanjia.cn.base.BaseActivity;
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
public class UserByDesignerInfoActivity extends BaseActivity {
	private static final String TAG = UserByDesignerInfoActivity.class
			.getName();

	@Override
	public void initView() {
		get_Designer_Info();
	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub

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
	public int getLayoutId() {
		return R.layout.activity_designer_info;
	}

}

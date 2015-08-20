package com.jianfanjia.cn.http;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.config.Url;
import com.loopj.android.http.AsyncHttpResponseHandler;

/*
 * 
 * @ClassName: JianFanJiaApi
 * @Description: http接口类
 * @author zhanghao
 * @date 2015-8-19 下午2:17:12
 * 
 */
public class JianFanJiaApiClient {

	/**
	 * @author zhanghao
	 * 
	 * @param username
	 * @param password
	 * @param handler
	 */
	public static void login(String username, String password,
			AsyncHttpResponseHandler handler) {
		// RequestParams params = new RequestParams();
		// params.put("phone", username);
		// params.put("pass", password);
		/*
		 * UserInfo user = new UserInfo(); user.setPhone(username);
		 * user.setPass(password);
		 */
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("phone", username);
			jsonParams.put("pass", password);
			StringEntity entity = new StringEntity(jsonParams.toString());
			HttpRestClient.post(MyApplication.context(), Url.LOGIN_URL, entity,
					"application/json", handler);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

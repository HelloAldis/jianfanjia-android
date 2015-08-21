package com.jianfanjia.cn.http;

import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.base.BaseApplication;
import com.jianfanjia.cn.bean.RegisterInfo;
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
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("phone", username);
			jsonParams.put("pass", password);
			StringEntity entity = new StringEntity(jsonParams.toString());
			HttpRestClient.post(BaseApplication.context(), Url.LOGIN_URL, entity,
					"application/json", handler);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @author zhanghao
	 * @param phone
	 * @param handler
	 */
	public static void send_verification(String phone,AsyncHttpResponseHandler handler){
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("phone", phone);
			StringEntity entity = new StringEntity(jsonParams.toString());
			HttpRestClient.post(BaseApplication.context(), Url.GET_CODE_URL, entity,
					"application/json", handler);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @author zhanghao
	 * @param registerInfo
	 * @param handler
	 */
	public static void register(RegisterInfo registerInfo,AsyncHttpResponseHandler handler){
			StringEntity entity;
			try {
				entity = new StringEntity(registerInfo.toString());
				HttpRestClient.post(BaseApplication.context(), Url.REGISTER_URL, entity,
						"application/json", handler);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	
	
	
	
	
}

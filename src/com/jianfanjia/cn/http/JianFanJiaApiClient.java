package com.jianfanjia.cn.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jianfanjia.cn.base.BaseApplication;
import com.jianfanjia.cn.bean.ProcedureInfo;
import com.jianfanjia.cn.bean.RegisterInfo;
import com.jianfanjia.cn.bean.SiteInfo;
import com.jianfanjia.cn.bean.UserInfo;
import com.jianfanjia.cn.config.Url;
import com.jianfanjia.cn.tools.StringUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
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
	
	/**
	 * @author zhanghao
	 * @param phone
	 * @param hanlder
	 */
	public static void getUserInfoByPhone(String phone,AsyncHttpResponseHandler hanlder){
		 StringEntity entity;
		 try{
			 entity = new StringEntity(phone);
			 HttpRestClient.post(BaseApplication.context(), Url.REGISTER_URL, entity,
						"application/json", hanlder);
		 } catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		 }
	}
	
	// 拿到所有的模拟工地数据
	public static ArrayList<SiteInfo> getAllSites(Context context) {
		ArrayList<SiteInfo> allSites = null;
		try {
			InputStream is = context.getAssets().open("site_json.txt");
			String jsonString = StringUtils.toConvertString(is);
			Gson gson = new Gson();
			allSites = gson.fromJson(jsonString,
					new TypeToken<ArrayList<SiteInfo>>() {
					}.getType());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return allSites;
	}

	// 拿到用户所拥有的工地
	public static ArrayList<SiteInfo> getAllSites(Context context,String phone,String userType) {
		ArrayList<SiteInfo> allSites = getAllSites(context);
		ArrayList<SiteInfo> userSite = new ArrayList<SiteInfo>();
		if (userType.equals(UserInfo.IDENTITY_DESIGNER)) {
			for (SiteInfo site : allSites) {
				if (site.getDesignerPhone().equals(phone)) {
					AddProcudureToSite(context, site);
					userSite.add(site);
				}
			}
		} else {
			for (SiteInfo site : allSites) {
				if (site.getUserPhone().equals(phone)) {
					AddProcudureToSite(context, site);
					userSite.add(site);
				}
			}
		}
		allSites.clear();
		return userSite;
	}

	// 根据工地拿到工序集合
	public static void AddProcudureToSite(Context context, SiteInfo site) {
		ArrayList<ProcedureInfo> allProcedure = null;
		try {
			InputStream is = context.getAssets().open(site.getUserPhone());
			String jsonString = StringUtils.toConvertString(is);
			Gson gson = new Gson();
			allProcedure = gson.fromJson(jsonString,
					new TypeToken<ArrayList<ProcedureInfo>>() {
					}.getType());
			site.setProcedures(allProcedure);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
}

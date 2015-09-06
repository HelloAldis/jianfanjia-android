package com.jianfanjia.cn.tools;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;

/**
 * 
 * @ClassName: JsonParser
 * @Description:json解析类
 * @author fengliang
 * @date 2015-8-18 下午1:16:47
 * 
 */
public class JsonParser {
	private static final String TAG = JsonParser.class.getName();

	/**
	 * json转化为javabean对象
	 * 
	 * @param json
	 * @param beanClass
	 * @return
	 */
	public static <T> T jsonToBean(String json, Class<T> beanClass) {
		T t = null;
		try {
			Gson gson = new Gson();
			t = gson.fromJson(json, beanClass);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}

	public static <T> List<T> jsonToList(String jsonString, Type typeOfT) {
		List<T> list = new ArrayList<T>();
		try {
			Gson gson = new Gson();
			list = gson.fromJson(jsonString, typeOfT);
		} catch (Exception e) {
		}
		return list;
	}

	/**
	 * javabean对象转化为Json
	 * 
	 * @param object
	 * @return
	 */
	public static String beanToJson(Object object) {
		String jsonObject = null;
		try {
			Gson gson = new Gson();
			jsonObject = gson.toJson(object);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonObject;
	}

	// 设计师获取工地列表
	// public static List<DesignerSiteInfo> getDesignerSiteList(String jsonStr)
	// {
	// List<DesignerSiteInfo> list = new ArrayList<DesignerSiteInfo>();
	// try {
	// JSONObject obj = new JSONObject(jsonStr);
	// JSONArray array = obj.getJSONArray("data");
	// LogTool.d(TAG, "array:" + array);
	// for (int i = 0; i < array.length(); i++) {
	// DesignerSiteInfo info = new DesignerSiteInfo();
	// JSONObject tempObj = array.getJSONObject(i);
	// info.setSiteid(tempObj.getString("_id"));
	// info.setCity(tempObj.getString("city"));
	// info.setDistrict(tempObj.getString("district"));
	// info.setCell(tempObj.getString("cell"));
	// info.setUserid(tempObj.getString("userid"));
	// info.setGoingon(tempObj.getString("going_on"));
	// JSONObject userObj = tempObj.getJSONObject("user");
	// User ownerInfo = new User();
	// ownerInfo.setOwnerid(userObj.getString("_id"));
	// ownerInfo.setImageid(userObj.getString("imageid"));
	// ownerInfo.setPhone(userObj.getString("phone"));
	// ownerInfo.setName(userObj.getString("username"));
	// info.setInfo(ownerInfo);
	// list.add(info);
	// }
	// return list;
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return null;
	// }
}

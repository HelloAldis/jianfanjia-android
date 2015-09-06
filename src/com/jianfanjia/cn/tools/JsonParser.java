package com.jianfanjia.cn.tools;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import com.google.gson.Gson;
import com.jianfanjia.cn.bean.DesignerSiteInfo;
import com.jianfanjia.cn.bean.OwnerInfo;
import com.jianfanjia.cn.bean.UserByOwnerInfo;

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

	// 业主获取自己的个人资料
	public static UserByOwnerInfo getUserByOwnerInfo(String jsonStr) {
		try {
			UserByOwnerInfo info = new UserByOwnerInfo();
			JSONObject obj = new JSONObject(jsonStr);
			JSONObject dataObj = obj.getJSONObject("data");
			LogTool.d(TAG, "dataObj:" + dataObj);
			info.setSex(dataObj.getString("sex"));
			info.setPhone(dataObj.getString("phone"));
			info.setImageId(dataObj.getString("imageid"));
			info.setCity(dataObj.getString("city"));
			info.setCommunication_type(dataObj.getString("communication_type"));
			info.setUsername(dataObj.getString("username"));
			info.setAddress(dataObj.getString("address"));
			info.setProvince(dataObj.getString("province"));
			info.setDistrict(dataObj.getString("district"));
			LogTool.d(TAG, "info:" + info);
			return info;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 设计师获取工地列表
	public static List<DesignerSiteInfo> getDesignerSiteList(String jsonStr) {
		List<DesignerSiteInfo> list = new ArrayList<DesignerSiteInfo>();
		try {
			JSONObject obj = new JSONObject(jsonStr);
			JSONArray array = obj.getJSONArray("data");
			LogTool.d(TAG, "array:" + array);
			for (int i = 0; i < array.length(); i++) {
				DesignerSiteInfo info = new DesignerSiteInfo();
				JSONObject tempObj = array.getJSONObject(i);
				info.setSiteid(tempObj.getString("_id"));
				info.setCity(tempObj.getString("city"));
				info.setDistrict(tempObj.getString("district"));
				info.setCell(tempObj.getString("cell"));
				info.setUserid(tempObj.getString("userid"));
				info.setGoingon(tempObj.getString("going_on"));
				JSONObject userObj = tempObj.getJSONObject("user");
				OwnerInfo ownerInfo = new OwnerInfo();
				ownerInfo.setOwnerid(userObj.getString("_id"));
				ownerInfo.setImageid(userObj.getString("imageid"));
				ownerInfo.setPhone(userObj.getString("phone"));
				ownerInfo.setName(userObj.getString("username"));
				info.setInfo(ownerInfo);
				list.add(info);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}

package com.jianfanjia.cn.tools;

import java.lang.reflect.Type;
import java.util.List;
import com.google.gson.Gson;

/**
 * 
 * @ClassName: JsonParser
 * @Description:json������
 * @author fengliang
 * @date 2015-8-18 ����1:16:47
 * 
 */
public class JsonParser {
	private static final String TAG = JsonParser.class.getName();

	/**
	 * jsonת��Ϊjavabean����
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
	 * jsonת��Ϊlist
	 * 
	 * @param jsonString
	 * @param typeOfT
	 * @return
	 */
	public static <T> List<T> jsonToList(String jsonString, Type typeOfT) {
		List<T> list = null;
		try {
			Gson gson = new Gson();
			list = gson.fromJson(jsonString, typeOfT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * javabean����ת��ΪJson
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

}

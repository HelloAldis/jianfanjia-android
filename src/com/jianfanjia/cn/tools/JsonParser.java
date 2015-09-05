package com.jianfanjia.cn.tools;

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

}

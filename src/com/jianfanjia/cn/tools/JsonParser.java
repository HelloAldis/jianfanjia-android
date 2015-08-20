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
	
	private Gson gson;
	
	public JsonParser(){
		gson = new Gson();
	}
	
	/**
	 * json转化为javabean对象
	 * @param json
	 * @param beanClass
	 * @return
	 */
	public <T> T jsonToBean(String json,Class<T> beanClass){
		return gson.fromJson(json, beanClass);
	}
	
	/**
	 * javabean对象转化为Json
	 * @param object
	 * @return
	 */
	public String beanToJson(Object object){
		String jsonObject = gson.toJson(object);
		return jsonObject;
	}

}

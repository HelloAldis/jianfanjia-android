package com.jianfanjia.cn.tools;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * 
 * @ClassName: JsonParser
 * @Description:json解析类
 * @author fengliang
 * @date 2015-8-18 下午1:16:47
 * 
 */
public class JsonParser {
	
	private static Gson gson;
	
	static{
		gson = new GsonBuilder().registerTypeHierarchyAdapter(Date.class,    
                new JsonSerializer<Date>() {    
            @SuppressLint("SimpleDateFormat") public JsonElement serialize(Date src,    
                    Type typeOfSrc,    
                    JsonSerializationContext context) {    
                SimpleDateFormat format = new SimpleDateFormat(    
                        "yyyy-MM-dd hh:MM:ss z");    
                return new JsonPrimitive(format.format(src));    
            }    
        }).setDateFormat("yyyy-MM-dd hh:MM:ss z").create();
	}
	
	/**
	 * json转化为javabean对象
	 * @param json
	 * @param beanClass
	 * @return
	 */
	public static <T> T jsonToBean(String json,Class<T> beanClass){
		return gson.fromJson(json, beanClass);
	}
	
	/**
	 * javabean对象转化为Json
	 * @param object
	 * @return
	 */
	public static String beanToJson(Object object){
		String jsonObject = gson.toJson(object);
		return jsonObject;
	}

}

package com.jianfanjia.cn.designer.tools;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.List;

/**
 * @author fengliang
 * @ClassName: JsonParser
 * @Description:json解析类
 * @date 2015-8-18 下午1:16:47
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
     * json转化为list
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

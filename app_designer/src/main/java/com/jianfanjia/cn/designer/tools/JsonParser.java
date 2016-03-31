package com.jianfanjia.cn.designer.tools;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

/**
 * Description:json解析类
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 12:33
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

    /**
     * 将map转为json格式字符串
     * @param param
     * @return
     */
    public static String MapToJson(Map<String, Object> param) {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(param);
        return jsonStr;
    }

}

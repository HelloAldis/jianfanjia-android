package com.jianfanjia.cn.http;

import com.google.gson.Gson;
import com.jianfanjia.cn.http.Listener.Parser;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Name: GsonParser
 * User: fengliang
 * Date: 2015-12-04
 * Time: 10:38
 */
public class GsonParser<T> implements Parser<T> {
    private Class<T> mClass = null;

    public GsonParser(Class<T> mClass) {
        this.mClass = mClass;
    }


    @Override
    public T parse(Response response) {
        try {
            Gson gson = new Gson();
            String str = response.body().string();
            T t = gson.fromJson(str, mClass);
            return t;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

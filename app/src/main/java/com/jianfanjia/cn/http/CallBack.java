package com.jianfanjia.cn.http;

import com.jianfanjia.cn.http.Listener.Parser;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Name: CallBack
 * User: fengliang
 * Date: 2015-12-04
 * Time: 10:52
 */
public class CallBack<T> implements com.squareup.okhttp.Callback {
    private Parser<T> parser;

    public CallBack(Parser<T> parser) {
        this.parser = parser;
    }

    @Override
    public void onFailure(Request request, IOException e) {

    }

    @Override
    public void onResponse(Response response) throws IOException {

    }
}

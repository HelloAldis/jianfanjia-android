package com.jianfanjia.cn.designer.http;

import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by Administrator on 2015/12/4.
 */
public class StringParser implements Parser<String> {
    @Override
    public String parse(Response response) {
        String result = null;
        try {
            result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}

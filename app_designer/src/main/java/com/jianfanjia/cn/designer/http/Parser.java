package com.jianfanjia.cn.designer.http;

import com.squareup.okhttp.Response;

/**
 * Created by Administrator on 2015/12/4.
 */
public interface Parser<T> {
    T parse(Response response);
}

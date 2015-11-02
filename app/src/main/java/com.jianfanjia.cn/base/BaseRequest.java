package com.jianfanjia.cn.base;

import android.content.Context;

import com.jianfanjia.cn.cache.DataManagerNew;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;

public class BaseRequest {

    private static final MediaType MEDIA_TYPE_STREAM = MediaType.parse("application/octet-stream;charset=utf-8");
    private static final MediaType MEDIA_TYPE_STRING = MediaType.parse("text/plain;charset=utf-8");
    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json;charset=utf-8");
    protected DataManagerNew dataManager;
    protected Context context;
    protected Request request;
    protected String url;

    public BaseRequest(Context context) {
        dataManager = DataManagerNew.getInstance();
        this.context = context;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    // 请求之前的准备操作
    public void pre() {

    }

    // 框架请求成功后的通用处理
    protected void all() {

    }

    // 数据正确后的处理
    public void onSuccess(BaseResponse baseResponse) {

    }

    // 数据错误后的处理
    public void onFailure(BaseResponse baseResponse) {
        String err_msg = baseResponse.getErr_msg();
    }

}

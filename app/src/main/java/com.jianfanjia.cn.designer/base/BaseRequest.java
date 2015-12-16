package com.jianfanjia.cn.designer.base;

import android.content.Context;

import com.jianfanjia.cn.designer.cache.DataManagerNew;
import com.jianfanjia.cn.designer.interf.ApiDataUpdateListenter;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;

/**
 * Description: com.jianfanjia.com.jianfanjia.com.jianfanjia.com.jianfanjia.cn.designer.designer.com.jianfanjia.com.jianfanjia.cn.designer.designer.com.jianfanjia.com.jianfanjia.cn.designer.designer.base
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-10-14 10:19
 */
public abstract class BaseRequest implements ApiDataUpdateListenter {
    private static final MediaType MEDIA_TYPE_STREAM = MediaType.parse("application/octet-stream;charset=utf-8");
    private static final MediaType MEDIA_TYPE_STRING = MediaType.parse("text/plain;charset=utf-8");
    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json;charset=utf-8");
    protected DataManagerNew dataManager;
    protected Context context;
    protected Request request;
    protected String url;
    protected MediaType mediaType = MEDIA_TYPE_JSON;//默认的请求方式方式

    public BaseRequest(Context context) {
        this.context = context;
        dataManager = DataManagerNew.getInstance();
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    // 请求之前的准备操作
    @Override
    public void pre() {

    }

    // 框架请求成功后的通用处理
    @Override
    public void all() {

    }

    // 数据正确后的处理
    @Override
    public void onSuccess(Object data) {

    }

    // 数据错误后的处理
    @Override
    public void onFailure(String error_msg) {
    }


}

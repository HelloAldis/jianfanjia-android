package com.jianfanjia.api.request.common;

import com.jianfanjia.api.request.BaseRequest;

import java.util.Map;

/**
 * Name: GetDecorateLiveRequest
 * User: fengliang
 * Date: 2016-03-28
 * Time: 17:27
 */
public class GetDecorateLiveRequest extends BaseRequest {

    private Map<String, Object> param;


    public Map<String, Object> getParam() {
        return param;
    }

    public void setParam(Map<String, Object> param) {
        this.param = param;
    }
}

package com.jianfanjia.api.request.common;

import com.jianfanjia.api.request.BaseRequest;

import java.util.Map;

/**
 * Name: GetUnReadMsgRequest
 * User: fengliang
 * Date: 2016-03-29
 * Time: 11:33
 */
public class GetUnReadMsgRequest extends BaseRequest {

    private Map<String, Object> param;

    public Map<String, Object> getParam() {
        return param;
    }

    public void setParam(Map<String, Object> param) {
        this.param = param;
    }
}

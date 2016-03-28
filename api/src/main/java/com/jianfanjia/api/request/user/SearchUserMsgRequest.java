package com.jianfanjia.api.request.user;

import com.jianfanjia.api.request.BaseRequest;

import java.util.Map;

/**
 * Name: SearchUserMsgRequest
 * User: fengliang
 * Date: 2016-03-28
 * Time: 18:32
 */
public class SearchUserMsgRequest extends BaseRequest {
    private Map<String, Object> param;

    public Map<String, Object> getParam() {
        return param;
    }

    public void setParam(Map<String, Object> param) {
        this.param = param;
    }
}

package com.jianfanjia.api.request.user;

import com.jianfanjia.api.request.BaseRequest;

/**
 * Name: BindingWeiXinRequest
 * User: fengliang
 * Date: 2016-03-28
 * Time: 15:05
 */
public class BindingWeiXinRequest extends BaseRequest {
    private String openid;
    private String unionid;

    public BindingWeiXinRequest(String openid, String unionid) {
        this.openid = openid;
        this.unionid = unionid;
    }
}
package com.jianfanjia.api.request.guest;

import com.jianfanjia.api.request.BaseRequest;

/**
 * Name: VerifyPhoneRequest
 * User: fengliang
 * Date: 2016-03-28
 * Time: 15:02
 */
public class VerifyPhoneRequest extends BaseRequest {
    private String phone;

    public VerifyPhoneRequest(String phone) {
        this.phone = phone;
    }
}  

package com.jianfanjia.api.request.guest;

import com.jianfanjia.api.request.BaseRequest;

/**
 * Name: SendVerificationRequest
 * User: fengliang
 * Date: 2016-03-28
 * Time: 15:29
 */
public class SendVerificationRequest extends BaseRequest {

    private String phone;

    public SendVerificationRequest(String phone) {
        this.phone = phone;
    }
}  

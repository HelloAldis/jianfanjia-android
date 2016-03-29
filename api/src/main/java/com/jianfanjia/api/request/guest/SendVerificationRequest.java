package com.jianfanjia.api.request.guest;

import com.jianfanjia.api.request.BaseRequest;

/**
 * Description: com.jianfanjia.api.request.user
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-03-28 16:22
 * Name: SendVerificationRequest
 * User: fengliang
 * Date: 2016-03-28
 * Time: 15:29
 */
public class SendVerificationRequest extends BaseRequest {

    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public SendVerificationRequest(String phone) {
        this.phone = phone;
    }
}  

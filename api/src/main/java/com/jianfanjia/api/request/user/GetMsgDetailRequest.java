package com.jianfanjia.api.request.user;

import com.jianfanjia.api.request.BaseRequest;

/**
 * Name: GetMsgDetailRequest
 * User: fengliang
 * Date: 2016-03-28
 * Time: 15:39
 */
public class GetMsgDetailRequest extends BaseRequest {

    private String messageid;

    public GetMsgDetailRequest(String messageid) {
        this.messageid = messageid;
    }

    public String getMessageid() {
        return messageid;
    }

    public void setMessageid(String messageid) {
        this.messageid = messageid;
    }
}

package com.jianfanjia.cn.http.request;

import android.content.Context;

import com.jianfanjia.cn.base.BaseRequest;

/**
 * Name: GetMsgDetailRequest
 * User: fengliang
 * Date: 2016-03-09
 * Time: 11:26
 */
public class GetMsgDetailRequest extends BaseRequest {
    private String messageid;

    public GetMsgDetailRequest(Context context, String messageid) {
        super(context);
        this.messageid = messageid;
        url = url_new.GET_USER_MSG_DETAIL;
    }
}

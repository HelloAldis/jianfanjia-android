package com.jianfanjia.cn.http.request;

import android.content.Context;

import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.tools.JsonParser;

import java.util.Map;

/**
 * Name: GetUnReadMsgRequest
 * User: fengliang
 * Date: 2016-03-09
 * Time: 11:23
 */
public class GetUnReadMsgRequest extends BaseRequest {
    private Map<String, Object> param;

    public GetUnReadMsgRequest(Context context, Map<String, Object> param) {
        super(context);
        this.param = param;
        url = url_new.GET_UNREAD_MSG_COUNT;
    }

    public String getParam() {
        return JsonParser.MapToJson(param);
    }
}

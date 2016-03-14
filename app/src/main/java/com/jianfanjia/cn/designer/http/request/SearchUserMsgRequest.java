package com.jianfanjia.cn.designer.http.request;

import android.content.Context;

import com.jianfanjia.cn.designer.base.BaseRequest;
import com.jianfanjia.cn.designer.tools.JsonParser;

import java.util.Map;

/**
 * Name: SearchUserMsgRequest
 * User: fengliang
 * Date: 2016-03-09
 * Time: 10:46
 */
public class SearchUserMsgRequest extends BaseRequest {
    private Map<String, Object> param;

    public SearchUserMsgRequest(Context context, Map<String, Object> param) {
        super(context);
        this.param = param;
        url = url_new.SEARCH_USER_MSG;
    }

    public String getParam() {
        return JsonParser.MapToJson(param);
    }
}

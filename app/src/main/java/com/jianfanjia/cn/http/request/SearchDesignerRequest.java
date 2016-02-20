package com.jianfanjia.cn.http.request;

import android.content.Context;

import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.tools.JsonParser;

import java.util.Map;

/**
 * Name: SearchDesignerRequest
 * User: fengliang
 * Date: 2016-02-20
 * Time: 09:52
 */
public class SearchDesignerRequest extends BaseRequest {

    private Map<String,Object> param;

    public SearchDesignerRequest(Context context, Map<String,Object> param) {
        super(context);
        this.param = param;
        url = url_new.SEARCH_DESIGNER;
    }

    public String getParam(){
        return JsonParser.MapToJson(param);
    }
}

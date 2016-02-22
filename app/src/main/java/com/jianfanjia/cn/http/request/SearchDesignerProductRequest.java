package com.jianfanjia.cn.http.request;

import android.content.Context;

import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.tools.JsonParser;

import java.util.Map;

/**
 * Name: SearchDesignerProductRequest
 * User: fengliang
 * Date: 2015-10-20
 * Time: 16:35
 */
public class SearchDesignerProductRequest extends BaseRequest {
    private Map<String, Object> param;

    public SearchDesignerProductRequest(Context context, Map<String, Object> param) {
        super(context);
        this.param = param;
        url = url_new.SEARCH_DESIGNER_PRODUCT;
    }

    public String getParam() {
        return JsonParser.MapToJson(param);
    }
}

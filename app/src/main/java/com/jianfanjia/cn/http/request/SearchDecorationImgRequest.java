package com.jianfanjia.cn.http.request;

import android.content.Context;

import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.tools.JsonParser;

import java.util.Map;

/**
 * Name: SearchDecorationImgRequest
 * User: fengliang
 * Date: 2015-12-08
 * Time: 17:01
 */
public class SearchDecorationImgRequest extends BaseRequest {
    private Map<String, Object> param;

    public SearchDecorationImgRequest(Context context, Map<String, Object> param) {
        super(context);
        this.param = param;
        url = url_new.SEARCH_DECORATION_IMG;
    }

    public String getParam() {
        return JsonParser.MapToJson(param);
    }
}

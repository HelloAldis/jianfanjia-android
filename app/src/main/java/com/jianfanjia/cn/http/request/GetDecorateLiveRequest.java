package com.jianfanjia.cn.http.request;

import android.content.Context;

import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.tools.JsonParser;

import java.util.Map;

/**
 * Description: com.jianfanjia.cn.http.request
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-03-11 14:23
 */
public class GetDecorateLiveRequest extends BaseRequest {

    private Map<String, Object> param;

    public GetDecorateLiveRequest(Context context,Map<String, Object> param) {
        super(context);
        this.param = param;
        url = url_new.SEARCH_SHARE;
    }

    public String getParam() {
        return JsonParser.MapToJson(param);
    }
}

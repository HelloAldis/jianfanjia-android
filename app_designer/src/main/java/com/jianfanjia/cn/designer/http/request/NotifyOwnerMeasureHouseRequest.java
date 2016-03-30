package com.jianfanjia.cn.designer.http.request;

import android.content.Context;

import com.jianfanjia.cn.designer.base.BaseRequest;
import com.jianfanjia.cn.designer.tools.JsonParser;

import java.util.Map;

/**
 * Description: com.jianfanjia.cn.designer.http.request
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-03-17 10:43
 */
public class NotifyOwnerMeasureHouseRequest extends BaseRequest {

    private Map<String, Object> param;

    public NotifyOwnerMeasureHouseRequest(Context context, Map<String, Object> param) {
        super(context);
        this.param = param;
        url = url_new.NOTIFY_OWNER_MEASURE_HOUSE;
    }

    public String getParam() {
        return JsonParser.MapToJson(param);
    }
}

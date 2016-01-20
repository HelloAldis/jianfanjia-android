package com.jianfanjia.cn.designer.http.request;

import android.content.Context;

import com.jianfanjia.cn.designer.base.BaseRequest;

/**
 * Description: com.jianfanjia.cn.designer.http.request
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-01-20 09:31
 */
public class GetAllRequirementListRequest extends BaseRequest {

    public GetAllRequirementListRequest(Context context) {
        super(context);
        url = url_new.GET_ALL_REQUIREMENT_LIST;
    }
}

package com.jianfanjia.cn.designer.http.request;

import android.content.Context;

import com.jianfanjia.cn.designer.base.BaseRequest;

/**
 * Description: com.jianfanjia.cn.designer.http.request
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-01-20 16:36
 */
public class RefuseRequirementRequest extends BaseRequest {

    public RefuseRequirementRequest(Context context) {
        super(context);
        url = url_new.REFUSE_REQUIREMENT;
    }
}

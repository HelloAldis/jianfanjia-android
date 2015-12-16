package com.jianfanjia.cn.designer.http.request;

import android.content.Context;

import com.jianfanjia.cn.designer.base.BaseRequest;
import com.jianfanjia.cn.designer.config.Url_New;

/**
 * 用户同意改期request
 * Created by Administrator on 2015/10/12.
 */
public class AgreeRescheduleRequest extends BaseRequest {

    public AgreeRescheduleRequest(Context context) {
        super(context);
        url = Url_New.AGREE_RESCHDULE;
    }

    @Override
    public void onSuccess(Object data) {
        super.onSuccess(data);
    }
}

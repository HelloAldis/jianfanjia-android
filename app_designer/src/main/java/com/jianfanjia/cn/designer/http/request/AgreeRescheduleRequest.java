package com.jianfanjia.cn.designer.http.request;

import android.content.Context;

import com.jianfanjia.cn.designer.base.BaseRequest;

/**
 * 用户同意改期request
 * Created by Administrator on 2015/10/12.
 */
public class AgreeRescheduleRequest extends BaseRequest{

    public AgreeRescheduleRequest(Context context) {
        super(context);
        url = url_new.AGREE_RESCHDULE;
    }

    @Override
    public void onSuccess(Object data) {
        super.onSuccess(data);
    }
}

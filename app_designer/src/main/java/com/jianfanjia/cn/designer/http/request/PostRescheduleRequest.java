package com.jianfanjia.cn.designer.http.request;

import android.content.Context;

import com.jianfanjia.cn.designer.base.BaseRequest;

/**
 * 用户改期request
 * Created by Administrator on 2015/10/12.
 */
public class PostRescheduleRequest extends BaseRequest{

    public PostRescheduleRequest(Context context) {
        super(context);
        url = url_new.POST_RESCHDULE;
    }

    @Override
    public void onSuccess(Object data) {
        super.onSuccess(data);
    }
}

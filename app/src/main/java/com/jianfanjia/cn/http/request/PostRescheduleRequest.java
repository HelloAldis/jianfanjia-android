package com.jianfanjia.cn.http.request;

import android.content.Context;

import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.config.Url;

/**
 * 用户改期request
 * Created by Administrator on 2015/10/12.
 */
public class PostRescheduleRequest extends BaseRequest{

    public PostRescheduleRequest(Context context) {
        super(context);
        url = Url.POST_RESCHDULE;
    }

    @Override
    public void onSuccess(Object data) {
        super.onSuccess(data);
    }
}

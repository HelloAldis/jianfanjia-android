package com.jianfanjia.cn.http.request;

import android.content.Context;

import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.base.BaseResponse;
import com.jianfanjia.cn.config.Url;

/**
 * 用户同意改期request
 * Created by Administrator on 2015/10/12.
 */
public class AgreeRescheduleRequest extends BaseRequest{

    public AgreeRescheduleRequest(Context context) {
        super(context);
        url = Url.AGREE_RESCHDULE;
    }

    @Override
    public void onSuccess(BaseResponse baseResponse) {
        super.onSuccess(baseResponse);
    }
}

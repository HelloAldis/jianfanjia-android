package com.jianfanjia.cn.http.request;

import android.content.Context;

import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.base.BaseResponse;
import com.jianfanjia.cn.config.Url;

/**
 * 用户拒绝改期request
 * Created by Administrator on 2015/10/12.
 */
public class RefuseRescheduleRequest extends BaseRequest{

    public RefuseRescheduleRequest(Context context) {
        super(context);
        url = Url.REFUSE_RESCHDULE;
    }

    @Override
    public void onSuccess(BaseResponse baseResponse) {
        super.onSuccess(baseResponse);
    }
}

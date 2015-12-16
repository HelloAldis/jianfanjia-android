package com.jianfanjia.cn.designer.http.request;

import android.content.Context;

import com.jianfanjia.cn.designer.base.BaseRequest;
import com.jianfanjia.cn.designer.config.Url_New;

/**
 * 用户拒绝改期request
 * Created by Administrator on 2015/10/12.
 */
public class RefuseRescheduleRequest extends BaseRequest {

    public RefuseRescheduleRequest(Context context) {
        super(context);
        url = Url_New.REFUSE_RESCHDULE;
    }

    @Override
    public void onSuccess(Object data) {
        super.onSuccess(data);
    }
}

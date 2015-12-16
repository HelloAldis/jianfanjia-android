package com.jianfanjia.cn.designer.http.request;

import android.content.Context;

import com.jianfanjia.cn.designer.base.BaseRequest;
import com.jianfanjia.cn.designer.config.Url_New;

/**
 * 用户获取所有的request
 * Created by Administrator on 2015/10/12.
 */
public class GetAllRescheduleRequest extends BaseRequest {

    public GetAllRescheduleRequest(Context context) {
        super(context);
        url = Url_New.GET_RESCHDULE_ALL;
    }

    @Override
    public void onSuccess(Object data) {
        super.onSuccess(data);
    }
}

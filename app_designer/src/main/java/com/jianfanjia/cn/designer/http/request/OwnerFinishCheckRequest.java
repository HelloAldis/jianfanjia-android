package com.jianfanjia.cn.designer.http.request;

import android.content.Context;

import com.jianfanjia.cn.designer.base.BaseRequest;

/**
 * Created by Administrator on 2015/10/12.
 */
public class OwnerFinishCheckRequest extends BaseRequest{
    public OwnerFinishCheckRequest(Context context) {
        super(context);
        url = url_new.CONFIRM_CHECK_DONE_BY_OWNER;
    }

    @Override
    public void onSuccess(Object data) {
        super.onSuccess(data);
    }
}

package com.jianfanjia.cn.http.request;

import android.content.Context;

import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.config.Url;

/**
 * Created by Administrator on 2015/10/12.
 */
public class NotifyOwnerCheckRequest extends BaseRequest{
    public NotifyOwnerCheckRequest(Context context) {
        super(context);
        url = Url.CONFIRM_CHECK_BY_DESIGNER;
    }

    @Override
    public void onSuccess(Object data) {
        super.onSuccess(data);
    }
}

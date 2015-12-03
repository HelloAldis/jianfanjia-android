package com.jianfanjia.cn.http.request;

import android.content.Context;

import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.config.Url_New;

/**
 * Created by Administrator on 2015/10/12.
 */
public class PostSectionFinishRequest extends BaseRequest {

    public PostSectionFinishRequest(Context context) {
        super(context);
        url = Url_New.POST_PROCESS_DONE_ITEM;
    }

    @Override
    public void onSuccess(Object data) {
        super.onSuccess(data);
    }
}
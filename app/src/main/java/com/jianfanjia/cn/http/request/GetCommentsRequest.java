package com.jianfanjia.cn.http.request;

import android.content.Context;

import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.config.Url_New;

/**
 * Name: GetCommentsRequest
 * User: fengliang
 * Date: 2015-10-23
 * Time: 19:42
 */
public class GetCommentsRequest extends BaseRequest {
    private String topicid;
    private int from;
    private int limit;

    public GetCommentsRequest(Context context, String topicid, int from, int limit) {
        super(context);
        this.topicid = topicid;
        this.from = from;
        this.limit = limit;
        url = Url_New.GET_COMMENT;
    }

    @Override
    public void pre() {
        super.pre();
    }

    @Override
    public void all() {
        super.all();
    }

    @Override
    public void onSuccess(Object data) {
        super.onSuccess(data);
    }
}
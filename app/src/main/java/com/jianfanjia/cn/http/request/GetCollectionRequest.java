package com.jianfanjia.cn.http.request;

import android.content.Context;

import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.config.Url_New;

/**
 * Name: GetCollectionRequest
 * User: fengliang
 * Date: 2015-12-08
 * Time: 16:00
 */
public class GetCollectionRequest extends BaseRequest {
    private int from;
    private int limit;

    public GetCollectionRequest(Context context, int from, int limit) {
        super(context);
        this.from = from;
        this.limit = limit;
        url = Url_New.GET_PRODUCT_LIST_BY_COLLECTED;
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

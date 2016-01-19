package com.jianfanjia.cn.designer.http.request;

import android.content.Context;

import com.jianfanjia.cn.designer.base.BaseRequest;

/**
 * Name: SearchDesignerProductRequest
 * User: fengliang
 * Date: 2015-10-20
 * Time: 16:35
 */
public class SearchDesignerProductRequest extends BaseRequest {
    private String designerid;
    private int from;
    private int limit;

    public SearchDesignerProductRequest(Context context, String designerid, int from, int limit) {
        super(context);
        this.designerid = designerid;
        this.from = from;
        this.limit = limit;
        url = url_new.SEARCH_DESIGNER_PRODUCT;
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
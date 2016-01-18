package com.jianfanjia.cn.designer.http.request;

import android.content.Context;

import com.jianfanjia.cn.designer.base.BaseRequest;

/**
 * Name: AddCollectionRequest
 * User: fengliang
 * Date: 2015-12-08
 * Time: 15:54
 */
public class AddCollectionRequest extends BaseRequest {
    private String productid;

    public AddCollectionRequest(Context context, String productid) {
        super(context);
        this.productid = productid;
        url = url_new.ADD_PRODUCT;
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

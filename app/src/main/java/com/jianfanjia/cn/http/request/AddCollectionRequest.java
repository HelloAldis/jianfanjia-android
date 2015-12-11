package com.jianfanjia.cn.http.request;

import android.content.Context;

import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.config.Url_New;

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
        url = Url_New.ADD_PRODUCT;
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

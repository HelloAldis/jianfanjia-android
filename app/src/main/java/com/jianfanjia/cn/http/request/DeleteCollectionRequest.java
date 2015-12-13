package com.jianfanjia.cn.http.request;

import android.content.Context;

import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.config.Url_New;

/**
 * Name: DeleteCollectionRequest
 * User: fengliang
 * Date: 2015-12-08
 * Time: 15:58
 */
public class DeleteCollectionRequest extends BaseRequest {
    private String productid;

    public DeleteCollectionRequest(Context context, String productid) {
        super(context);
        this.productid = productid;
        url = Url_New.DELETE_PRODUCT_BY_USER;
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

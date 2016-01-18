package com.jianfanjia.cn.designer.http.request;

import android.content.Context;

import com.jianfanjia.cn.designer.base.BaseRequest;

/**
 * Name: GetOrderedDesignerRequest
 * User: fengliang
 * Date: 2015-10-22
 * Time: 15:14
 */
public class GetOrderedDesignerRequest extends BaseRequest {
    private String requirementid;

    public GetOrderedDesignerRequest(Context context, String requirementid) {
        super(context);
        this.requirementid = requirementid;
        url = url_new.USER_ORDERD_DESIGNERS;
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

package com.jianfanjia.cn.designer.http.request;

import android.content.Context;

import com.jianfanjia.cn.designer.base.BaseRequest;

/**
 * Name: GetOrderDesignerListByUserRequest
 * User: fengliang
 * Date: 2015-10-21
 * Time: 19:13
 */
public class GetOrderDesignerListByUserRequest extends BaseRequest {
    private String requirementid = null;

    public GetOrderDesignerListByUserRequest(Context context, String requirementid) {
        super(context);
        this.requirementid = requirementid;
        url = url_new.REQUIREMENT_ORDER_DESIGNER_LIST;
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
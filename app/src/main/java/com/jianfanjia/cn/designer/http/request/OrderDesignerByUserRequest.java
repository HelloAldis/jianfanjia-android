package com.jianfanjia.cn.designer.http.request;

import android.content.Context;

import com.jianfanjia.cn.designer.base.BaseRequest;

import java.util.List;

/**
 * Name: OrderDesignerByUserRequest
 * User: fengliang
 * Date: 2015-10-22
 * Time: 10:01
 */
public class OrderDesignerByUserRequest extends BaseRequest {
    private String requirementid;
    private List<String> designerids;

    public OrderDesignerByUserRequest(Context context, String requirementid, List<String> designerids) {
        super(context);
        this.requirementid = requirementid;
        this.designerids = designerids;
        url = url_new.USER_ORDER_DESIGNER;
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

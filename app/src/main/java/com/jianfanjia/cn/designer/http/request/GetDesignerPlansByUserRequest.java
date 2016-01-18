package com.jianfanjia.cn.designer.http.request;

import android.content.Context;

import com.jianfanjia.cn.designer.base.BaseRequest;

/**
 * Name: GetDesignerPlansByUserRequest
 * User: fengliang
 * Date: 2015-10-22
 * Time: 15:37
 */
public class GetDesignerPlansByUserRequest extends BaseRequest {
    private String requirementid;
    private String designerid;

    public GetDesignerPlansByUserRequest(Context context, String requirementid, String designerid) {
        super(context);
        this.requirementid = requirementid;
        this.designerid = designerid;
        url = url_new.USER_REQUIREMENT_PLANS;
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

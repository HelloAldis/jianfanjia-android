package com.jianfanjia.cn.designer.http.request;

import android.content.Context;

import com.jianfanjia.cn.designer.base.BaseRequest;

/**
 * Name: GetContractRequest
 * User: fengliang
 * Date: 2015-10-23
 * Time: 09:38
 */
public class GetContractRequest extends BaseRequest {
    private String requirementid;


    public GetContractRequest(Context context, String requirementid) {
        super(context);
        this.requirementid = requirementid;
        url = url_new.ONE_CONTRACT;
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


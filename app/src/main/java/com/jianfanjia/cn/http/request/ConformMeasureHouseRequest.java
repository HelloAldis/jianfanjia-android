package com.jianfanjia.cn.http.request;

import android.content.Context;

import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.config.Url_New;

/**
 * Name: GetOrderedDesignerRequest
 * User: fengliang
 * Date: 2015-10-22
 * Time: 15:14
 */
public class ConformMeasureHouseRequest extends BaseRequest {
    private String requirementid;

    public ConformMeasureHouseRequest(Context context, String requirementid) {
        super(context);
        this.requirementid = requirementid;
        url = Url_New.USER_ORDERD_DESIGNERS;
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

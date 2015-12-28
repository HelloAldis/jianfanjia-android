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
    private String designerid;

    public ConformMeasureHouseRequest(Context context, String requirementid,String designerid) {
        super(context);
        this.requirementid = requirementid;
        this.designerid = designerid;
        url = url_new.DESIGNER_HOUSE_CHECKED ;
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

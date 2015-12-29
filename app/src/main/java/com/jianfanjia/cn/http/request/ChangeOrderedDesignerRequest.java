package com.jianfanjia.cn.http.request;

import android.content.Context;

import com.jianfanjia.cn.base.BaseRequest;

/**
 * Name: ChangeOrderedDesignerRequest
 * User: fengliang
 * Date: 2015-10-26
 * Time: 12:38
 */
public class ChangeOrderedDesignerRequest extends BaseRequest {
    private String requirementid;
    private String old_designerid;
    private String new_designerid;

    public ChangeOrderedDesignerRequest(Context context, String requirementid, String old_designerid, String new_designerid) {
        super(context);
        this.requirementid = requirementid;
        this.old_designerid = old_designerid;
        this.new_designerid = new_designerid;
        url = url_new.USER_CHANGE_ORDERD_DESIGNER;
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

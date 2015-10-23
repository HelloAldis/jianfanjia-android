package com.jianfanjia.cn.http.request;

import android.content.Context;

import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.config.Url_New;

/**
 * Name: EvaluateDesignerRequest
 * User: fengliang
 * Date: 2015-10-23
 * Time: 09:14
 */
public class EvaluateDesignerRequest extends BaseRequest {
    private String requirementid;
    private String designerid;
    private int service_attitude;
    private int respond_speed;
    private String comment;
    private String is_anonymous;

    public EvaluateDesignerRequest(Context context, String requirementid, String designerid, int service_attitude, int respond_speed, String comment, String is_anonymous) {
        super(context);
        this.requirementid = requirementid;
        this.designerid = designerid;
        this.service_attitude = service_attitude;
        this.respond_speed = respond_speed;
        this.comment = comment;
        this.is_anonymous = is_anonymous;
        url = Url_New.EVALUATE_DESIGNER_BY_USER;
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

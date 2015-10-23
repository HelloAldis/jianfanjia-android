package com.jianfanjia.cn.http.request;

import android.content.Context;

import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.config.Url_New;

/**
 * Description:com.jianfanjia.cn.http.request
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-22 22:47
 */
public class ChoosePlanByUserRequest extends BaseRequest {
    private String requirementid;
    private String designerid;
    private String planid;

    public ChoosePlanByUserRequest(Context context, String requirementid, String designerid, String planid) {
        super(context);
        this.requirementid = requirementid;
        this.designerid = designerid;
        this.planid = planid;
        url = Url_New.USER_CHOOSE_PLAN;
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
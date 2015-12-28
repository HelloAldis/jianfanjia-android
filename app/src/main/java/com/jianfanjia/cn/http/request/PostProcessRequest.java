package com.jianfanjia.cn.http.request;

import android.content.Context;

import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.config.Url_New;

public class PostProcessRequest extends BaseRequest {
    private String requirementid;
    private String final_planid;

    public PostProcessRequest(Context context, String requirementid, String final_planid) {
        super(context);
        this.requirementid = requirementid;
        this.final_planid = final_planid;
        url = url_new.PROCESS;
    }

    @Override
    public void all() {
        // TODO Auto-generated method stub
        super.all();

    }

    @Override
    public void pre() {
        // TODO Auto-generated method stub
        super.pre();
    }

    @Override
    public void onSuccess(Object data) {
        if (data != null) {

        }
    }


}

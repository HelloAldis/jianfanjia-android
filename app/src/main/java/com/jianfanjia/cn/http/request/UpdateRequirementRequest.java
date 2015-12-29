package com.jianfanjia.cn.http.request;

import android.content.Context;

import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.bean.RequirementInfo;
import com.jianfanjia.cn.config.Url_New;

public class UpdateRequirementRequest extends BaseRequest {

    private RequirementInfo requirementInfo;

    public UpdateRequirementRequest(Context context, RequirementInfo requirementInfo) {
        super(context);
        this.requirementInfo = requirementInfo;
        url = url_new.REQUIREMENT_UPDATE;
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

    public RequirementInfo getRequirementInfo() {
        return requirementInfo;
    }

    public void setRequirementInfo(RequirementInfo requirementInfo) {
        this.requirementInfo = requirementInfo;
    }

}

package com.jianfanjia.cn.designer.http.request;

import android.content.Context;

import com.jianfanjia.cn.designer.base.BaseRequest;
import com.jianfanjia.cn.designer.bean.ProcessInfo;
import com.jianfanjia.cn.designer.bean.RequirementInfo;
import com.jianfanjia.cn.designer.tools.JsonParser;

public class PostRequirementRequest extends BaseRequest {

    private RequirementInfo requirementInfo;

    public PostRequirementRequest(Context context, RequirementInfo requirementInfo) {
        super(context);
        this.requirementInfo = requirementInfo;
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
        if (data.toString() != null) {
            ProcessInfo processInfo = JsonParser
                    .jsonToBean(data.toString(), ProcessInfo.class);
            if (processInfo != null) {
                dataManager.saveProcessInfo(processInfo);
                dataManager.setCurrentProcessInfo(processInfo);
            }
        }
    }

    public RequirementInfo getRequirementInfo() {
        return requirementInfo;
    }

    public void setRequirementInfo(RequirementInfo requirementInfo) {
        this.requirementInfo = requirementInfo;
    }

}

package com.jianfanjia.cn.designer.http.request;

import android.content.Context;

import com.jianfanjia.cn.designer.base.BaseRequest;
import com.jianfanjia.cn.designer.bean.RequirementInfo;
import com.jianfanjia.cn.designer.tools.JsonParser;

public class GetRequirementRequest extends BaseRequest {

    public GetRequirementRequest(Context context) {
        super(context);
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
            RequirementInfo requirementInfo = JsonParser.jsonToBean(data.toString(),
                    RequirementInfo.class);
            if (requirementInfo != null && requirementInfo.get_id() != null) {
                requirementInfo.setRequirementid(requirementInfo.get_id());
            }
            dataManager.setRequirementInfo(requirementInfo);
        }
    }


}

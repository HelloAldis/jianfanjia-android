package com.jianfanjia.cn.designer.http.request;

import android.content.Context;

import com.jianfanjia.cn.designer.base.BaseRequest;

public class GetRequirementListRequest extends BaseRequest {

    public GetRequirementListRequest(Context context) {
        super(context);
        url = url_new.REQUIREMENT_LIST;
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
            /*Requirement requirementInfo = JsonParser.jsonToBean((String) data,
                    Requirement.class);
            if (requirementInfo != null && requirementInfo.get_id() != null) {
                requirementInfo.set_id(requirementInfo.get_id());
            }
            dataManager.setRequirementInfo(requirementInfo);*/


        }
    }


}
package com.jianfanjia.cn.http.request;

import android.content.Context;

import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.bean.RequirementInfo;
import com.jianfanjia.cn.config.Url_New;
import com.jianfanjia.cn.tools.JsonParser;

public class GetRequirementRequest extends BaseRequest {

    public GetRequirementRequest(Context context) {
        super(context);
        url = Url_New.REQUIREMENT_LIST;
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
            RequirementInfo requirementInfo = JsonParser.jsonToBean((String) data,
                    RequirementInfo.class);
            if (requirementInfo != null && requirementInfo.get_id() != null) {
                requirementInfo.set_id(requirementInfo.get_id());
            }
            dataManager.setRequirementInfo(requirementInfo);
        }
    }


}

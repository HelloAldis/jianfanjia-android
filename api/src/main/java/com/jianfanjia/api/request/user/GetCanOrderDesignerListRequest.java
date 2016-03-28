package com.jianfanjia.api.request.user;

import com.jianfanjia.api.request.BaseRequest;

/**
 * Description: com.jianfanjia.api.request.user
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-03-28 11:15
 */
public class GetCanOrderDesignerListRequest extends BaseRequest{

    private String requirementis;

    public String getRequirementis() {
        return requirementis;
    }

    public void setRequirementis(String requirementis) {
        this.requirementis = requirementis;
    }
}

package com.jianfanjia.api.request.designer;

import com.jianfanjia.api.request.BaseRequest;

/**
 * Name: ResponseRequirementRequest
 * User: fengliang
 * Date: 2016-03-31
 * Time: 14:45
 */
public class ResponseRequirementRequest extends BaseRequest {
    private String requirementid;

    public String getRequirementid() {
        return requirementid;
    }

    public void setRequirementid(String requirementid) {
        this.requirementid = requirementid;
    }

}

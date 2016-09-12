package com.jianfanjia.api.request.designer;

import com.jianfanjia.api.request.BaseRequest;

/**
 * Name: RefuseRequirementRequest
 * User: fengliang
 * Date: 2016-03-31
 * Time: 14:46
 */
public class RefuseRequirementRequest extends BaseRequest {
    private String requirementid;
    private String reject_respond_msg;

    public String getRequirementid() {
        return requirementid;
    }

    public void setRequirementid(String requirementid) {
        this.requirementid = requirementid;
    }

    public String getReject_respond_msg() {
        return reject_respond_msg;
    }

    public void setReject_respond_msg(String reject_respond_msg) {
        this.reject_respond_msg = reject_respond_msg;
    }
}

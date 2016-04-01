package com.jianfanjia.api.request.designer;

import com.jianfanjia.api.request.BaseRequest;

/**
 * Name: NotifyOwnerMeasureHouseRequest
 * User: fengliang
 * Date: 2016-03-31
 * Time: 14:31
 */
public class NotifyOwnerMeasureHouseRequest extends BaseRequest {

    private String planid;

    private String userid;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPlanid() {
        return planid;
    }

    public void setPlanid(String planid) {
        this.planid = planid;
    }
}

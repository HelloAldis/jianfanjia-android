package com.jianfanjia.api.request.designer;

import com.jianfanjia.api.request.BaseRequest;

/**
 * Name: NotifyOwnerMeasureHouseRequest
 * User: fengliang
 * Date: 2016-03-31
 * Time: 14:31
 */
public class NotifyOwnerMeasureHouseRequest extends BaseRequest {

    private String _id;

    private String userid;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}

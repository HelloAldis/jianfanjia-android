package com.jianfanjia.cn.designer.bean;

import java.io.Serializable;

/**
 * Name: RescheduleInfo
 * User: fengliang
 * Date: 2016-03-11
 * Time: 13:33
 */
public class RescheduleInfo implements Serializable {
    private String _id;
    private long request_date;
    private String processid;
    private String userid;
    private String designerid;
    private String section;
    private long new_date;
    private String request_role;
    private String status;
    private int __v;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public long getRequest_date() {
        return request_date;
    }

    public void setRequest_date(long request_date) {
        this.request_date = request_date;
    }

    public String getProcessid() {
        return processid;
    }

    public void setProcessid(String processid) {
        this.processid = processid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getDesignerid() {
        return designerid;
    }

    public void setDesignerid(String designerid) {
        this.designerid = designerid;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public long getNew_date() {
        return new_date;
    }

    public void setNew_date(long new_date) {
        this.new_date = new_date;
    }

    public String getRequest_role() {
        return request_role;
    }

    public void setRequest_role(String request_role) {
        this.request_role = request_role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int get__v() {
        return __v;
    }

    public void set__v(int __v) {
        this.__v = __v;
    }
}

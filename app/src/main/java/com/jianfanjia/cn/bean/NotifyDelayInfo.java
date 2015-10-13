package com.jianfanjia.cn.bean;

import java.io.Serializable;

/**
 * @author zhanghao
 * @Class NotifyDelayInfo.class
 * @Decription 延期提醒实体类
 * @date 2015-8-26 下午18:00
 */
public class NotifyDelayInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String _id;

    private String status;

    private String designerid;

    private long request_date;

    private int __v;

    private String userid;

    private String request_role;

    private long new_date;

    private String section;

    private String processid;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDesignerid() {
        return designerid;
    }

    public void setDesignerid(String designerid) {
        this.designerid = designerid;
    }

    public long getRequest_date() {
        return request_date;
    }

    public void setRequest_date(long request_date) {
        this.request_date = request_date;
    }

    public int get__v() {
        return __v;
    }

    public void set__v(int __v) {
        this.__v = __v;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getRequest_role() {
        return request_role;
    }

    public void setRequest_role(String request_role) {
        this.request_role = request_role;
    }

    public long getNew_date() {
        return new_date;
    }

    public void setNew_date(long new_date) {
        this.new_date = new_date;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getProcessid() {
        return processid;
    }

    public void setProcessid(String processid) {
        this.processid = processid;
    }

}

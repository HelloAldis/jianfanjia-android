package com.jianfanjia.cn.bean;

import java.io.Serializable;

/**
 * Description: com.jianfanjia.cn.bean
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-10-22 16:09
 */
public class Evaluation implements Serializable{

    private static final long serialVersionUID = -2742232970423153563L;
    private String _id;
    private String designerid;
    private String requiremengid;
    private String userid;
    private String is_anonymous;
    private String comment;
    private float respond_speed;
    private float service_attitude;
    private int _v;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getDesignerid() {
        return designerid;
    }

    public void setDesignerid(String designerid) {
        this.designerid = designerid;
    }

    public String getRequiremengid() {
        return requiremengid;
    }

    public void setRequiremengid(String requiremengid) {
        this.requiremengid = requiremengid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getIs_anonymous() {
        return is_anonymous;
    }

    public void setIs_anonymous(String is_anonymous) {
        this.is_anonymous = is_anonymous;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public float getRespond_speed() {
        return respond_speed;
    }

    public void setRespond_speed(float respond_speed) {
        this.respond_speed = respond_speed;
    }

    public float getService_attitude() {
        return service_attitude;
    }

    public void setService_attitude(float service_attitude) {
        this.service_attitude = service_attitude;
    }

    public int get_v() {
        return _v;
    }

    public void set_v(int _v) {
        this._v = _v;
    }
}

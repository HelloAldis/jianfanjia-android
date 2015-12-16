package com.jianfanjia.cn.designer.bean;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = -1450173592300692043L;

    private String _id;

    private String imageid;

    private String phone;

    private String username;

    public String getImageid() {
        return imageid;
    }

    public void setImageid(String imageid) {
        this.imageid = imageid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}

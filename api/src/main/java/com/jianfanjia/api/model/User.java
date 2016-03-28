package com.jianfanjia.api.model;

import java.io.Serializable;

/**
 * Created by Aldis on 16/3/28.
 */
public class User extends BaseModel implements Serializable {
    private static final long serialVersionUID = -1450173592300692043L;

    private String _id;
    private String imageid;
    private String phone;
    private String username;

    private String pass;
    private String usertype;
    private boolean is_wechat_first_login;
    private String wechat_openid;
    private String wechat_unionid;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

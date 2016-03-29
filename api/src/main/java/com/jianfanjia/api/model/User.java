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

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public boolean is_wechat_first_login() {
        return is_wechat_first_login;
    }

    public void setIs_wechat_first_login(boolean is_wechat_first_login) {
        this.is_wechat_first_login = is_wechat_first_login;
    }

    public String getWechat_openid() {
        return wechat_openid;
    }

    public void setWechat_openid(String wechat_openid) {
        this.wechat_openid = wechat_openid;
    }

    public String getWechat_unionid() {
        return wechat_unionid;
    }

    public void setWechat_unionid(String wechat_unionid) {
        this.wechat_unionid = wechat_unionid;
    }
}

package com.jianfanjia.cn.designer.bean;

import java.io.Serializable;

/**
 * Description:com.jianfanjia.cn.bean
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-20 21:47
 */
public class Designer implements Serializable {
    private static final long serialVersionUID = -8515828649644437770L;
    private String _id;
    private String phone;
    private String imageid;
    private String auth_type;
    private String username;
    private float respond_speed;
    private float service_attitude;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAuth_type() {
        return auth_type;
    }

    public void setAuth_type(String auth_type) {
        this.auth_type = auth_type;
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
}

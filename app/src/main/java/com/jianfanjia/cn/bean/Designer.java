package com.jianfanjia.cn.bean;

import java.io.Serializable;

/**
 * Description:com.jianfanjia.cn.bean
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-20 21:47
 */
public class Designer implements Serializable {
    private String _id;
    private String imageid;
    private String username;

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
}

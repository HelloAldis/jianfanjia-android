package com.jianfanjia.cn.bean;

import java.io.Serializable;

/**
 * Description: com.jianfanjia.cn.bean
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-10-20 10:05
 */
public class OrderDesignerInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String _id;//设计师id
    private String imageid;//设计师头像id
    private String username;//设计师名称
    private PlanInfo plan;//设计师方案

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

    public PlanInfo getPlan() {
        return plan;
    }

    public void setPlan(PlanInfo plan) {
        this.plan = plan;
    }
}

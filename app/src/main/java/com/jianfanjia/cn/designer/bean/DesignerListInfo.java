package com.jianfanjia.cn.designer.bean;

import java.io.Serializable;

/**
 * Name: DesignerListInfo
 * User: fengliang
 * Date: 2015-10-14
 * Time: 14:05
 */
public class DesignerListInfo implements Serializable {
    private static final long serialVersionUID = 5084168531582372675L;
    private String _id;
    private String auth_type;
    private String imageid;
    private String username;
    private Product product;

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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getAuth_type() {
        return auth_type;
    }

    public void setAuth_type(String auth_type) {
        this.auth_type = auth_type;
    }
}

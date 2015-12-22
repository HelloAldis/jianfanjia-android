package com.jianfanjia.cn.bean;

import java.io.Serializable;

/**
 * Name: PriceDetail
 * User: fengliang
 * Date: 2015-10-23
 * Time: 12:05
 */
public class PriceDetail implements Serializable {
    private static final long serialVersionUID = 5886154173353451274L;
    private String _id;
    private String item;
    private float price;
    private String description;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}  

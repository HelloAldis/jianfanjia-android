package com.jianfanjia.api.model;

import java.io.Serializable;

/**
 * Created by Aldis on 16/3/28.
 */
public class PlanPriceDetail extends BaseModel implements Serializable {
    private static final long serialVersionUID = 5886154173353451274L;
    private String _id;
    private String item;
    private String price;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

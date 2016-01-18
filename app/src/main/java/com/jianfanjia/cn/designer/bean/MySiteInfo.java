package com.jianfanjia.cn.designer.bean;

import java.io.Serializable;

/**
 * Name: MySiteInfo
 * User: fengliang
 * Date: 2016-01-18
 * Time: 14:18
 */
public class MySiteInfo implements Serializable {
    private String _id;
    private String imgid;
    private String cell;
    private String section;
    private String date;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getImgid() {
        return imgid;
    }

    public void setImgid(String imgid) {
        this.imgid = imgid;
    }

    public String getCell() {
        return cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

package com.jianfanjia.cn.bean;

import java.io.Serializable;

/**
 * Name: ImageInfo
 * User: fengliang
 * Date: 2015-10-20
 * Time: 11:37
 */
public class ImageInfo implements Serializable {
    private static final long serialVersionUID = 8429692010603248547L;
    private String description;
    private String imageid;
    private String section;
    private String _id;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageid() {
        return imageid;
    }

    public void setImageid(String imageid) {
        this.imageid = imageid;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}

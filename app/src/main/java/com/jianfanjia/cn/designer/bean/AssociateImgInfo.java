package com.jianfanjia.cn.designer.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Name: AssociateImgInfo
 * User: fengliang
 * Date: 2015-12-22
 * Time: 16:49
 */
public class AssociateImgInfo implements Serializable {
    private String _id;
    private List<Img> images;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public List<Img> getImages() {
        return images;
    }

    public void setImages(List<Img> images) {
        this.images = images;
    }
}

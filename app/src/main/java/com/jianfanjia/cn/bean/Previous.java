package com.jianfanjia.cn.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Name: Previous
 * User: fengliang
 * Date: 2016-02-16
 * Time: 16:43
 */
public class Previous implements Serializable {
    private List<AssociateImgInfo> beautiful_images;
    private int total;

    public List<AssociateImgInfo> getBeautiful_images() {
        return beautiful_images;
    }

    public void setBeautiful_images(List<AssociateImgInfo> beautiful_images) {
        this.beautiful_images = beautiful_images;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}

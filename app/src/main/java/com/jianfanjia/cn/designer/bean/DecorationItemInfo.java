package com.jianfanjia.cn.designer.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Name: DecorationItemInfo
 * User: fengliang
 * Date: 2015-12-07
 * Time: 10:33
 */
public class DecorationItemInfo implements Serializable {
    private static final long serialVersionUID = 1882322068000269988L;
    private List<BeautyImgInfo> beautiful_images;
    private int total;

    public List<BeautyImgInfo> getBeautiful_images() {
        return beautiful_images;
    }

    public void setBeautiful_images(List<BeautyImgInfo> beautiful_images) {
        this.beautiful_images = beautiful_images;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}

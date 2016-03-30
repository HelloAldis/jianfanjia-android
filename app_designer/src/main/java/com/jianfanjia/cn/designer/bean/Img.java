package com.jianfanjia.cn.designer.bean;

import java.io.Serializable;

/**
 * Name: Img
 * User: fengliang
 * Date: 2015-12-16
 * Time: 14:10
 */
public class Img implements Serializable {
    private static final long serialVersionUID = -2396378567042666240L;
    private String imageid;
    private int width;
    private int height;

    public String getImageid() {
        return imageid;
    }

    public void setImageid(String imageid) {
        this.imageid = imageid;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}  

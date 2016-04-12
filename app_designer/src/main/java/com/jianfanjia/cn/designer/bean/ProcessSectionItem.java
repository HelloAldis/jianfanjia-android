package com.jianfanjia.cn.designer.bean;

import java.io.Serializable;

/**
 * Name: ProcessSectionItem
 * User: fengliang
 * Date: 2016-01-20
 * Time: 10:48
 */
public class ProcessSectionItem implements Serializable {
    private int res;
    private String title;

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

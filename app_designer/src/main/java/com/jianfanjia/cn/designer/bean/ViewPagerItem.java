package com.jianfanjia.cn.designer.bean;

import java.io.Serializable;

public class ViewPagerItem implements Serializable {

    private static final long serialVersionUID = 3760854448859466132L;
    private int resId;
    private String title;
    private String date;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

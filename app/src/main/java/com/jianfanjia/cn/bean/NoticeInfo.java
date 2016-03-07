package com.jianfanjia.cn.bean;

import java.io.Serializable;

/**
 * Name: NoticeInfo
 * User: fengliang
 * Date: 2016-03-07
 * Time: 09:25
 */
public class NoticeInfo implements Serializable {
    private String title;
    private long time;
    private String type;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

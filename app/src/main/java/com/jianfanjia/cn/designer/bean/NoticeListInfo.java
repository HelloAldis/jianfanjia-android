package com.jianfanjia.cn.designer.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Name: NoticeListInfo
 * User: fengliang
 * Date: 2016-03-09
 * Time: 16:28
 */
public class NoticeListInfo implements Serializable {

    private List<NoticeInfo> list;

    private int total;

    public List<NoticeInfo> getList() {
        return list;
    }

    public void setList(List<NoticeInfo> list) {
        this.list = list;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}

package com.jianfanjia.cn.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author zhanghao
 * @class SectionInfo.class
 * @Decription 此类是工序信息实体类
 * @date 2015-8-31 上午11:50
 */
public class SectionInfo implements Serializable {

    private static final long serialVersionUID = 7265654008649361788L;

    private String _id;

    private long start_at;

    private long end_at;

    private String name;

    private String status;

    private ArrayList<SectionItemInfo> items;

    private CheckInfo ys;

    private RescheduleInfo reschedule;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getStart_at() {
        return start_at;
    }

    public void setStart_at(long start_at) {
        this.start_at = start_at;
    }

    public long getEnd_at() {
        return end_at;
    }

    public void setEnd_at(long end_at) {
        this.end_at = end_at;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<SectionItemInfo> getItems() {
        return items;
    }

    public void setItems(ArrayList<SectionItemInfo> items) {
        this.items = items;
    }

    public CheckInfo getYs() {
        return ys;
    }

    public void setYs(CheckInfo ys) {
        this.ys = ys;
    }

    public RescheduleInfo getReschedule() {
        return reschedule;
    }

    public void setReschedule(RescheduleInfo reschedule) {
        this.reschedule = reschedule;
    }

    public SectionItemInfo getSectionItemInfoByName(String itemName) {
        if (items != null) {
            for (SectionItemInfo sectionitem : items) {
                if (sectionitem.getName().equals(itemName)) {
                    return sectionitem;
                }
            }
        }
        return null;
    }

}

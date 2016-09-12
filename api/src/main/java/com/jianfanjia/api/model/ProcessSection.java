package com.jianfanjia.api.model;

import java.util.List;

/**
 * Created by Aldis on 16/3/28.
 */
public class ProcessSection extends BaseModel {
    private String _id;
    private long start_at;
    private long end_at;
    private String name;
    private String status;
    private String label;
    private List<ProcessSectionItem> items;
    private ProcessSectionYs ys;
    private Reschedule reschedule;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ProcessSectionItem> getItems() {
        return items;
    }

    public void setItems(List<ProcessSectionItem> items) {
        this.items = items;
    }

    public ProcessSectionYs getYs() {
        return ys;
    }

    public void setYs(ProcessSectionYs ys) {
        this.ys = ys;
    }

    public Reschedule getReschedule() {
        return reschedule;
    }

    public void setReschedule(Reschedule reschedule) {
        this.reschedule = reschedule;
    }

    public ProcessSectionItem getSectionItemInfoByName(String itemName) {
        if (items != null) {
            for (ProcessSectionItem sectionitem : items) {
                if (sectionitem.getName().equals(itemName)) {
                    return sectionitem;
                }
            }
        }
        return null;
    }
}

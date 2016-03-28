package com.jianfanjia.api.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Aldis on 16/3/28.
 */
public class ProcessSectionItem extends BaseModel implements Serializable {

    private static final long serialVersionUID = -8274501297058657269L;

    private boolean isOpen;
    private String _id;
    private String name;
    private String status;
    private long date;
    private int comment_count;
    private List<String> images;

    public boolean isOpen() {
        return isOpen;
    }

    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
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

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}

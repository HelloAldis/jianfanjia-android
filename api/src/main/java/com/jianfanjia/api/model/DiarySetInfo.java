package com.jianfanjia.api.model;

import java.util.List;

/**
 * Description: com.jianfanjia.api.model
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-06-13 10:53
 */
public class DiarySetInfo extends BaseModel {

    private String _id;
    private String authorid;
    private String usertype;
    private String cover_imageid;
    private String title;
    private int house_area;
    private String house_type;
    private String dec_style;
    private String work_type;
    private long create_at;
    private long lastupdate;
    private int view_count;
    private String latest_section_label;
    private List<DiaryInfo> diaries;
    private User author;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getAuthorid() {
        return authorid;
    }

    public void setAuthorid(String authorid) {
        this.authorid = authorid;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public String getCover_imageid() {
        return cover_imageid;
    }

    public void setCover_imageid(String cover_imageid) {
        this.cover_imageid = cover_imageid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getHouse_area() {
        return house_area;
    }

    public void setHouse_area(int house_area) {
        this.house_area = house_area;
    }

    public String getHouse_type() {
        return house_type;
    }

    public void setHouse_type(String house_type) {
        this.house_type = house_type;
    }

    public String getDec_style() {
        return dec_style;
    }

    public void setDec_style(String dec_style) {
        this.dec_style = dec_style;
    }

    public String getWork_type() {
        return work_type;
    }

    public void setWork_type(String work_type) {
        this.work_type = work_type;
    }

    public long getCreate_at() {
        return create_at;
    }

    public void setCreate_at(long create_at) {
        this.create_at = create_at;
    }

    public long getLastupdate() {
        return lastupdate;
    }

    public void setLastupdate(long lastupdate) {
        this.lastupdate = lastupdate;
    }

    public int getView_count() {
        return view_count;
    }

    public void setView_count(int view_count) {
        this.view_count = view_count;
    }

    public String getLatest_section_label() {
        return latest_section_label;
    }

    public void setLatest_section_label(String latest_section_label) {
        this.latest_section_label = latest_section_label;
    }

    public List<DiaryInfo> getDiaries() {
        return diaries;
    }

    public void setDiaries(List<DiaryInfo> diaries) {
        this.diaries = diaries;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }
}

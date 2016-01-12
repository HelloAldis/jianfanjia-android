package com.jianfanjia.cn.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Name: BeautyImgInfo
 * User: fengliang
 * Date: 2015-12-08
 * Time: 17:38
 */
public class BeautyImgInfo implements Serializable {

    private static final long serialVersionUID = 3756904014887565122L;
    private String _id;
    private boolean is_deleted;
    private long lastupdate;
    private long create_at;
    private String title;
    private String description;
    private String dec_type;
    private String house_type;
    private String dec_style;
    private String status;
    private String keywords;
    private String authorid;
    private String usertype;
    private String section;
    private int __v;
    private int favorite_count;
    private int view_count;
    private boolean is_my_favorite;
    private List<AssociateImgInfo> associate_beautiful_images;
    private List<Img> images;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDec_type() {
        return dec_type;
    }

    public void setDec_type(String dec_type) {
        this.dec_type = dec_type;
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

    public List<Img> getImages() {
        return images;
    }

    public void setImages(List<Img> images) {
        this.images = images;
    }

    public long getLastupdate() {
        return lastupdate;
    }

    public void setLastupdate(long lastupdate) {
        this.lastupdate = lastupdate;
    }

    public long getCreate_at() {
        return create_at;
    }

    public void setCreate_at(long create_at) {
        this.create_at = create_at;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public int get__v() {
        return __v;
    }

    public void set__v(int __v) {
        this.__v = __v;
    }

    public int getFavorite_count() {
        return favorite_count;
    }

    public void setFavorite_count(int favorite_count) {
        this.favorite_count = favorite_count;
    }

    public int getView_count() {
        return view_count;
    }

    public void setView_count(int view_count) {
        this.view_count = view_count;
    }

    public boolean is_my_favorite() {
        return is_my_favorite;
    }

    public void setIs_my_favorite(boolean is_my_favorite) {
        this.is_my_favorite = is_my_favorite;
    }

    public boolean is_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(boolean is_deleted) {
        this.is_deleted = is_deleted;
    }

    public List<AssociateImgInfo> getAssociate_beautiful_images() {
        return associate_beautiful_images;
    }

    public void setAssociate_beautiful_images(List<AssociateImgInfo> associate_beautiful_images) {
        this.associate_beautiful_images = associate_beautiful_images;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }
}

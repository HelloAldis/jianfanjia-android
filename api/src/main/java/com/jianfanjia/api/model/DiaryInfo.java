package com.jianfanjia.api.model;

import android.text.SpannableString;

import java.util.List;

/**
 * Description: com.jianfanjia.api.model
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-06-13 10:52
 */
public class DiaryInfo extends BaseModel {

    private String _id;
    private String authorid;
    private String usertype;
    private String diarySetid;
    private String content;
    private String section_label;
    private long create_at;
    private long lastupdate;
    private int view_count;
    private int favorite_count;
    private int comment_count;
    private List<DiaryImageDetailInfo> images;
    private DiarySetInfo diarySet;
    private boolean is_my_favorite;
    private User author;
    private boolean is_deleted;
    private long refreshTime;
    private transient SpannableString showContent;
    private transient int showHeight;

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

    public String getDiarySetid() {
        return diarySetid;
    }

    public void setDiarySetid(String diarySetid) {
        this.diarySetid = diarySetid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSection_label() {
        return section_label;
    }

    public void setSection_label(String section_label) {
        this.section_label = section_label;
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

    public int getFavorite_count() {
        return favorite_count;
    }

    public void setFavorite_count(int favorite_count) {
        this.favorite_count = favorite_count;
    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    public List<DiaryImageDetailInfo> getImages() {
        return images;
    }

    public void setImages(List<DiaryImageDetailInfo> images) {
        this.images = images;
    }

    public DiarySetInfo getDiarySet() {
        return diarySet;
    }

    public void setDiarySet(DiarySetInfo diarySet) {
        this.diarySet = diarySet;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
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

    public long getRefreshTime() {
        return refreshTime;
    }

    public void setRefreshTime(long refreshTime) {
        this.refreshTime = refreshTime;
    }


    public int getShowHeight() {
        return showHeight;
    }

    public void setShowHeight(int showHeight) {
        this.showHeight = showHeight;
    }

    public SpannableString getShowContent() {
        return showContent;
    }

    public void setShowContent(SpannableString showContent) {
        this.showContent = showContent;
    }
}


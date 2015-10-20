package com.jianfanjia.cn.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Description: com.jianfanjia.cn.bean
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-10-20 09:49
 */
public class PlanInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userid;
    private String _id;
    private String designerid;
    private String requirementid;
    private int duration;
    private float total_price;
    private List<PriceDetail> price_detail;
    private String description;
    private String manager;
    private List<String> images;
    private String status;
    private long house_check_time;
    private long request_date;
    private long last_status_update_time;
    private List<CommentInfo> comments;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getDesignerid() {
        return designerid;
    }

    public void setDesignerid(String designerid) {
        this.designerid = designerid;
    }

    public String getRequirementid() {
        return requirementid;
    }

    public void setRequirementid(String requirementid) {
        this.requirementid = requirementid;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public List<PriceDetail> getPrice_detail() {
        return price_detail;
    }

    public void setPrice_detail(List<PriceDetail> price_detail) {
        this.price_detail = price_detail;
    }

    public void setTotal_price(float total_price) {
        this.total_price = total_price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getHouse_check_time() {
        return house_check_time;
    }

    public void setHouse_check_time(long house_check_time) {
        this.house_check_time = house_check_time;
    }

    public long getRequest_date() {
        return request_date;
    }

    public void setRequest_date(long request_date) {
        this.request_date = request_date;
    }

    public long getLast_status_update_time() {
        return last_status_update_time;
    }

    public void setLast_status_update_time(long last_status_update_time) {
        this.last_status_update_time = last_status_update_time;
    }

    public List<CommentInfo> getComments() {
        return comments;
    }

    public void setComments(List<CommentInfo> comments) {
        this.comments = comments;
    }

    public class PriceDetail {
        private String item;
        private float price;
        private String description;

        public String getItem() {
            return item;
        }

        public void setItem(String item) {
            this.item = item;
        }

        public float getPrice() {
            return price;
        }

        public void setPrice(float price) {
            this.price = price;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }


}

package com.jianfanjia.cn.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Name: PlandetailInfo
 * User: fengliang
 * Date: 2015-10-23
 * Time: 14:23
 */
public class PlandetailInfo implements Serializable {
    private String _id;
    private long last_status_update_time;
    private long request_date;
    private String designerid;
    private String userid;
    private String requirementid;
    private int __v;
    private long house_check_time;
    private int project_price_before_discount;
    private int total_design_fee;
    private int project_price_after_discount;
    private String manager;
    private String description;
    private int total_price;
    private int duration;
    private String status;
    private List<String> images;
    private List<PriceDetail> price_detail;
    private Designer designer;
    private User user;
    private RequirementInfo requirement;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public long getLast_status_update_time() {
        return last_status_update_time;
    }

    public void setLast_status_update_time(long last_status_update_time) {
        this.last_status_update_time = last_status_update_time;
    }

    public long getRequest_date() {
        return request_date;
    }

    public void setRequest_date(long request_date) {
        this.request_date = request_date;
    }

    public String getDesignerid() {
        return designerid;
    }

    public void setDesignerid(String designerid) {
        this.designerid = designerid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getRequirementid() {
        return requirementid;
    }

    public void setRequirementid(String requirementid) {
        this.requirementid = requirementid;
    }

    public int get__v() {
        return __v;
    }

    public void set__v(int __v) {
        this.__v = __v;
    }

    public long getHouse_check_time() {
        return house_check_time;
    }

    public void setHouse_check_time(long house_check_time) {
        this.house_check_time = house_check_time;
    }

    public int getProject_price_before_discount() {
        return project_price_before_discount;
    }

    public void setProject_price_before_discount(int project_price_before_discount) {
        this.project_price_before_discount = project_price_before_discount;
    }

    public int getTotal_design_fee() {
        return total_design_fee;
    }

    public void setTotal_design_fee(int total_design_fee) {
        this.total_design_fee = total_design_fee;
    }

    public int getProject_price_after_discount() {
        return project_price_after_discount;
    }

    public void setProject_price_after_discount(int project_price_after_discount) {
        this.project_price_after_discount = project_price_after_discount;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTotal_price() {
        return total_price;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<PriceDetail> getPrice_detail() {
        return price_detail;
    }

    public void setPrice_detail(List<PriceDetail> price_detail) {
        this.price_detail = price_detail;
    }

    public Designer getDesigner() {
        return designer;
    }

    public void setDesigner(Designer designer) {
        this.designer = designer;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public RequirementInfo getRequirement() {
        return requirement;
    }

    public void setRequirement(RequirementInfo requirement) {
        this.requirement = requirement;
    }
}

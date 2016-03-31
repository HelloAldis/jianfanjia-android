package com.jianfanjia.api.model;

import java.io.Serializable;
import java.util.List;

/**
 * Name: Plan
 * User: fengliang
 * Date: 2015-10-23
 * Time: 14:23
 */
public class Plan extends BaseModel implements Serializable {
    private static final long serialVersionUID = 4176946137457671385L;

    private String _id;
    private String name;
    private long last_status_update_time;
    private long request_date;
    private String designerid;
    private String userid;
    private String requirementid;
    private long house_check_time;
    private int project_price_before_discount;
    private int total_design_fee;
    private int project_price_after_discount;
    private String manager;
    private String description;
    private int total_price;
    private int duration;
    private int comment_count;
    private String status;
    private List<String> images;
    private List<PlanPriceDetail> price_detail;
    private Designer designer;
    private User user;
    private Requirement requirement;
    private String reject_respond_msg;

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

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
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

    public List<PlanPriceDetail> getPrice_detail() {
        return price_detail;
    }

    public void setPrice_detail(List<PlanPriceDetail> price_detail) {
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

    public Requirement getRequirement() {
        return requirement;
    }

    public void setRequirement(Requirement requirement) {
        this.requirement = requirement;
    }

    public String getReject_respond_msg() {
        return reject_respond_msg;
    }

    public void setReject_respond_msg(String reject_respond_msg) {
        this.reject_respond_msg = reject_respond_msg;
    }
}

package com.jianfanjia.cn.bean;

import java.io.Serializable;

/**
 * Description: com.jianfanjia.cn.bean
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-10-20 10:05
 */
public class OrderDesignerInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String _id;//设计师id
    private String imageid;//设计师头像id
    private String username;//设计师名称
    private String phone;
    private String city;
    private String provice;
    private int deal_done_count;
    private String work_auth_type;
    private String email_auth_type;
    private String uid_auth_type;
    private String auth_type;
    private int authed_product_count;
    private int order_count;
    private String status;
    private boolean is_rec;
    private PlanInfo plan;//设计师方案
    private Evaluation evaluation;
    private int match;

    public int getMatch() {
        return match;
    }

    public void setMatch(int match) {
        this.match = match;
    }

    public Evaluation getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(Evaluation evaluation) {
        this.evaluation = evaluation;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getImageid() {
        return imageid;
    }

    public void setImageid(String imageid) {
        this.imageid = imageid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public PlanInfo getPlan() {
        return plan;
    }

    public void setPlan(PlanInfo plan) {
        this.plan = plan;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvice() {
        return provice;
    }

    public void setProvice(String provice) {
        this.provice = provice;
    }

    public int getDeal_done_count() {
        return deal_done_count;
    }

    public void setDeal_done_count(int deal_done_count) {
        this.deal_done_count = deal_done_count;
    }

    public String getWork_auth_type() {
        return work_auth_type;
    }

    public void setWork_auth_type(String work_auth_type) {
        this.work_auth_type = work_auth_type;
    }

    public String getEmail_auth_type() {
        return email_auth_type;
    }

    public void setEmail_auth_type(String email_auth_type) {
        this.email_auth_type = email_auth_type;
    }

    public String getUid_auth_type() {
        return uid_auth_type;
    }

    public void setUid_auth_type(String uid_auth_type) {
        this.uid_auth_type = uid_auth_type;
    }

    public String getAuth_type() {
        return auth_type;
    }

    public void setAuth_type(String auth_type) {
        this.auth_type = auth_type;
    }

    public int getAuthed_product_count() {
        return authed_product_count;
    }

    public void setAuthed_product_count(int authed_product_count) {
        this.authed_product_count = authed_product_count;
    }

    public int getOrder_count() {
        return order_count;
    }

    public void setOrder_count(int order_count) {
        this.order_count = order_count;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean is_rec() {
        return is_rec;
    }

    public void setIs_rec(boolean is_rec) {
        this.is_rec = is_rec;
    }
}

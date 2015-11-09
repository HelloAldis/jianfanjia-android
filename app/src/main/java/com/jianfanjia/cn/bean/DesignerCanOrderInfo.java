package com.jianfanjia.cn.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Name: DesignerCanOrderInfo
 * User: fengliang
 * Date: 2015-10-22
 * Time: 09:45
 */
public class DesignerCanOrderInfo implements Serializable {
    private String _id;
    private String auth_type;
    private int order_count;
    private int dec_fee_all;
    private int dec_fee_half;
    private List<String> dec_house_types;
    private List<String> dec_districts;
    private List<String> dec_styles;
    private String communication_type;
    private String district;
    private String city;
    private String province;
    private String username;
    private String imageid;
    private int authed_product_count;
    private String uid_auth_type;
    private String email_auth_type;
    private String work_auth_type;
    private float respond_speed;
    private float service_attitude;
    private int match;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getAuth_type() {
        return auth_type;
    }

    public void setAuth_type(String auth_type) {
        this.auth_type = auth_type;
    }

    public int getOrder_count() {
        return order_count;
    }

    public void setOrder_count(int order_count) {
        this.order_count = order_count;
    }


    public int getDec_fee_half() {
        return dec_fee_half;
    }

    public void setDec_fee_half(int dec_fee_half) {
        this.dec_fee_half = dec_fee_half;
    }

    public List<String> getDec_house_types() {
        return dec_house_types;
    }

    public void setDec_house_types(List<String> dec_house_types) {
        this.dec_house_types = dec_house_types;
    }

    public List<String> getDec_districts() {
        return dec_districts;
    }

    public void setDec_districts(List<String> dec_districts) {
        this.dec_districts = dec_districts;
    }

    public List<String> getDec_styles() {
        return dec_styles;
    }

    public void setDec_styles(List<String> dec_styles) {
        this.dec_styles = dec_styles;
    }

    public String getCommunication_type() {
        return communication_type;
    }

    public void setCommunication_type(String communication_type) {
        this.communication_type = communication_type;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageid() {
        return imageid;
    }

    public void setImageid(String imageid) {
        this.imageid = imageid;
    }

    public int getAuthed_product_count() {
        return authed_product_count;
    }

    public void setAuthed_product_count(int authed_product_count) {
        this.authed_product_count = authed_product_count;
    }

    public String getUid_auth_type() {
        return uid_auth_type;
    }

    public void setUid_auth_type(String uid_auth_type) {
        this.uid_auth_type = uid_auth_type;
    }

    public String getEmail_auth_type() {
        return email_auth_type;
    }

    public void setEmail_auth_type(String email_auth_type) {
        this.email_auth_type = email_auth_type;
    }

    public String getWork_auth_type() {
        return work_auth_type;
    }

    public void setWork_auth_type(String work_auth_type) {
        this.work_auth_type = work_auth_type;
    }

    public int getMatch() {
        return match;
    }

    public void setMatch(int match) {
        this.match = match;
    }

    public int getDec_fee_all() {
        return dec_fee_all;
    }

    public void setDec_fee_all(int dec_fee_all) {
        this.dec_fee_all = dec_fee_all;
    }

    public float getRespond_speed() {
        return respond_speed;
    }

    public void setRespond_speed(float respond_speed) {
        this.respond_speed = respond_speed;
    }

    public float getService_attitude() {
        return service_attitude;
    }

    public void setService_attitude(float service_attitude) {
        this.service_attitude = service_attitude;
    }
}

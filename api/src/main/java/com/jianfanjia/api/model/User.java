package com.jianfanjia.api.model;

import java.util.ArrayList;

/**
 * Created by Aldis on 16/3/28.
 */
public class User extends BaseModel {

    private String _id;
    private String imageid;
    private String phone;
    private String username;
    private String pass;
    private String usertype;
    private boolean is_wechat_first_login;
    private String wechat_openid;
    private String wechat_unionid;
    private String accessToken;
    private String address;
    private String district;
    private String city;
    private String sex;
    private String communication_type;
    private String province;
    private boolean is_block;
    private int score;
    private String family_description;
    private String dec_progress;
    private ArrayList<String> dec_styles;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public boolean is_wechat_first_login() {
        return is_wechat_first_login;
    }

    public void setIs_wechat_first_login(boolean is_wechat_first_login) {
        this.is_wechat_first_login = is_wechat_first_login;
    }

    public String getWechat_openid() {
        return wechat_openid;
    }

    public void setWechat_openid(String wechat_openid) {
        this.wechat_openid = wechat_openid;
    }

    public String getWechat_unionid() {
        return wechat_unionid;
    }

    public void setWechat_unionid(String wechat_unionid) {
        this.wechat_unionid = wechat_unionid;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCommunication_type() {
        return communication_type;
    }

    public void setCommunication_type(String communication_type) {
        this.communication_type = communication_type;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public boolean is_block() {
        return is_block;
    }

    public void setIs_block(boolean is_block) {
        this.is_block = is_block;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getFamily_description() {
        return family_description;
    }

    public void setFamily_description(String family_description) {
        this.family_description = family_description;
    }

    public String getDec_progress() {
        return dec_progress;
    }

    public void setDec_progress(String dec_progress) {
        this.dec_progress = dec_progress;
    }

    public ArrayList<String> getDec_styles() {
        return dec_styles;
    }

    public void setDec_styles(ArrayList<String> dec_styles) {
        this.dec_styles = dec_styles;
    }
}

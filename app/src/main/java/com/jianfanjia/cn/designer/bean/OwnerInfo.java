package com.jianfanjia.cn.designer.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author zhanghao
 * @ClassName: OwerInfo
 * @Description: 我的业主个人信息类（设计师）
 * @date 2015-8-26 下午19:33
 */
public class OwnerInfo implements Serializable {

    private static final long serialVersionUID = 8598943994953102276L;

    private String _id;

    private String accessToken;

    private String pass;

    private String phone;

    private String __v;

    private String address;

    private String district;

    private String city;

    private String sex;

    private String username;

    private String communication_type;

    private String imageid;

    private String province;

    private boolean is_block;

    private int score;

    private String wechat_openid;

    private String wechat_unionid;

    private String family_description;

    private String dec_progress;

    private ArrayList<String> dec_styles;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String get__v() {
        return __v;
    }

    public void set__v(String __v) {
        this.__v = __v;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCommunication_type() {
        return communication_type;
    }

    public void setCommunication_type(String communication_type) {
        this.communication_type = communication_type;
    }

    public String getImageid() {
        return imageid;
    }

    public void setImageid(String imageid) {
        this.imageid = imageid;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
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

    public boolean is_block() {
        return is_block;
    }

    public void setIs_block(boolean is_block) {
        this.is_block = is_block;
    }
}
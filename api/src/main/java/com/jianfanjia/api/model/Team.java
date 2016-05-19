package com.jianfanjia.api.model;

import java.io.Serializable;

/**
 * Description: com.jianfanjia.api.model
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-05-19 14:41
 */
public class Team implements Serializable{

    private static final long serialVersionUID = -1553424198123287816L;
    private String _id;
    private String manager;
    private String designerid;
    private String uid;
    private String uid_image1;
    private String uid_image2;
    private String company;
    private int work_year;
    private String good_at;
    private String working_on;
    private String sex;
    private String province;
    private String city;
    private String district;
    private long create_at;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getDesignerid() {
        return designerid;
    }

    public void setDesignerid(String designerid) {
        this.designerid = designerid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUid_image1() {
        return uid_image1;
    }

    public void setUid_image1(String uid_image1) {
        this.uid_image1 = uid_image1;
    }

    public String getUid_image2() {
        return uid_image2;
    }

    public void setUid_image2(String uid_image2) {
        this.uid_image2 = uid_image2;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public int getWork_year() {
        return work_year;
    }

    public void setWork_year(int work_year) {
        this.work_year = work_year;
    }

    public String getGood_at() {
        return good_at;
    }

    public void setGood_at(String good_at) {
        this.good_at = good_at;
    }

    public String getWorking_on() {
        return working_on;
    }

    public void setWorking_on(String working_on) {
        this.working_on = working_on;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public long getCreate_at() {
        return create_at;
    }

    public void setCreate_at(long create_at) {
        this.create_at = create_at;
    }
}

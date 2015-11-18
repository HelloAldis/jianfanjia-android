package com.jianfanjia.cn.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhanghao
 * @ClassName: DesignerInfo
 * @Description: TODO
 * @date 2015-9-2 11:52
 */
public class DesignerInfo implements Serializable {
    private String _id;

    private String phone;

    private String __v;

    private String philosophy;

    private String achievement;

    private String design_fee_range;

    private String company;

    private String uid;

    private String address;

    private String district;

    private String city;

    private String sex;

    private String username;

    private String imageid;

    private String province;

    private String agreee_license;

    private String auth_type;

    private int team_count;

    private int product_count;

    private int order_count;

    private int view_count;

    private int dec_fee_all;

    private int dec_fee_half;

    private float respond_speed;

    private float service_attitude;

    private List<String> dec_house_types;

    private List<String> dec_districts;

    private List<String> dec_styles;

    private List<String> dec_types;

    private String communication_type;

    private boolean is_block;

    private int match;

    private float score;

    private boolean is_my_favorite;

    private int work_year;

    private String email;

    private String bank;

    private String bank_card;

    private String university;

    public boolean is_my_favorite() {
        return is_my_favorite;
    }

    public void setIs_my_favorite(boolean is_my_favorite) {
        this.is_my_favorite = is_my_favorite;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
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

    public String getPhilosophy() {
        return philosophy;
    }

    public void setPhilosophy(String philosophy) {
        this.philosophy = philosophy;
    }

    public String getAchievement() {
        return achievement;
    }

    public void setAchievement(String achievement) {
        this.achievement = achievement;
    }

    public String getDesign_fee_range() {
        return design_fee_range;
    }

    public void setDesign_fee_range(String design_fee_range) {
        this.design_fee_range = design_fee_range;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getAgreee_license() {
        return agreee_license;
    }

    public void setAgreee_license(String agreee_license) {
        this.agreee_license = agreee_license;
    }

    public String getAuth_type() {
        return auth_type;
    }

    public void setAuth_type(String auth_type) {
        this.auth_type = auth_type;
    }

    public int getTeam_count() {
        return team_count;
    }

    public void setTeam_count(int team_count) {
        this.team_count = team_count;
    }

    public int getProduct_count() {
        return product_count;
    }

    public void setProduct_count(int product_count) {
        this.product_count = product_count;
    }

    public int getOrder_count() {
        return order_count;
    }

    public void setOrder_count(int order_count) {
        this.order_count = order_count;
    }

    public int getView_count() {
        return view_count;
    }

    public void setView_count(int view_count) {
        this.view_count = view_count;
    }

    public int getDec_fee_all() {
        return dec_fee_all;
    }

    public void setDec_fee_all(int dec_fee_all) {
        this.dec_fee_all = dec_fee_all;
    }

    public int getDec_fee_half() {
        return dec_fee_half;
    }

    public void setDec_fee_half(int dec_fee_half) {
        this.dec_fee_half = dec_fee_half;
    }

    public List<String> getDec_styles() {
        return dec_styles;
    }

    public void setDec_styles(List<String> dec_styles) {
        this.dec_styles = dec_styles;
    }

    public List<String> getDec_districts() {
        return dec_districts;
    }

    public void setDec_districts(List<String> dec_districts) {
        this.dec_districts = dec_districts;
    }

    public List<String> getDec_house_types() {
        return dec_house_types;
    }

    public void setDec_house_types(List<String> dec_house_types) {
        this.dec_house_types = dec_house_types;
    }

    public String getCommunication_type() {
        return communication_type;
    }

    public void setCommunication_type(String communication_type) {
        this.communication_type = communication_type;
    }

    public boolean isIs_block() {
        return is_block;
    }

    public void setIs_block(boolean is_block) {
        this.is_block = is_block;
    }

    public int getMatch() {
        return match;
    }

    public void setMatch(int match) {
        this.match = match;
    }

    public List<String> getDec_types() {
        return dec_types;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public void setDec_types(List<String> dec_types) {
        this.dec_types = dec_types;
    }

    public boolean is_block() {
        return is_block;
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

    public int getWork_year() {
        return work_year;
    }

    public void setWork_year(int work_year) {
        this.work_year = work_year;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getBank_card() {
        return bank_card;
    }

    public void setBank_card(String bank_card) {
        this.bank_card = bank_card;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }
}
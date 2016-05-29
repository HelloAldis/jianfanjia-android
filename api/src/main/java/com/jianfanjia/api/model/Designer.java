package com.jianfanjia.api.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:com.jianfanjia.cn.bean
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-20 21:47
 */
public class Designer extends BaseModel {
    private String _id;
    private String phone;
    private String philosophy;
    private String achievement;
    private String design_fee_range;
    private String company;
    private String uid;
    private String uid_image1;
    private String uid_image2;
    private String address;
    private String district;
    private String city;
    private String sex;
    private String username;
    private String realname;
    private String imageid;
    private String province;
    private String pass;
    private String usertype;
    private String agreee_license;
    private String auth_type;
    private String auth_message;
    private long auth_date;
    private String uid_auth_type;
    private String uid_auth_message;
    private long uid_auth_date;
    private String email_auth_type;
    private String email_auth_message;
    private long email_auth_date;
    private String work_auth_type;
    private String work_auth_message;
    private long work_auth_date;
    private List<String> tags;
    private int team_count;
    private int product_count;
    private int authed_product_count;
    private int order_count;
    private int view_count;
    private int dec_fee_all;
    private int dec_fee_half;
    private float respond_speed;
    private float service_attitude;
    private List<String> dec_house_types = new ArrayList<>();
    private List<String> dec_districts = new ArrayList<>();
    private List<String> dec_styles = new ArrayList<>();
    private List<String> dec_types = new ArrayList<>();
    private List<String> work_types = new ArrayList<>();
    private List<DesignerAwardInfo> award_details = new ArrayList<>();
    private String diploma_imageid;
    private String communication_type;
    private boolean is_block;
    private int match;
    private float score;
    private boolean is_my_favorite;
    private int work_year;
    private String email;
    private String bank;
    private String bank_card;
    private String bank_card_image1;
    private String university;
    private Plan plan;//设计师方案
    private Evaluation evaluation;
    private Requirement requirement;
    private List<Product> products;

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

    public List<DesignerAwardInfo> getAward_details() {
        return award_details;
    }

    public String getDiploma_imageid() {
        return diploma_imageid;
    }

    public void setDiploma_imageid(String diploma_imageid) {
        this.diploma_imageid = diploma_imageid;
    }

    public void setAward_details(List<DesignerAwardInfo> award_details) {
        this.award_details = award_details;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
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

    public String getBank_card_image1() {
        return bank_card_image1;
    }

    public void setBank_card_image1(String bank_card_image1) {
        this.bank_card_image1 = bank_card_image1;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
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

    public String getUid_auth_type() {
        return uid_auth_type;
    }

    public void setUid_auth_type(String uid_auth_type) {
        this.uid_auth_type = uid_auth_type;
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

    public List<String> getDec_types() {
        return dec_types;
    }

    public void setDec_types(List<String> dec_types) {
        this.dec_types = dec_types;
    }

    public String getCommunication_type() {
        return communication_type;
    }

    public void setCommunication_type(String communication_type) {
        this.communication_type = communication_type;
    }

    public List<String> getWork_types() {
        return work_types;
    }

    public void setWork_types(List<String> work_types) {
        this.work_types = work_types;
    }

    public boolean is_block() {
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

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public boolean is_my_favorite() {
        return is_my_favorite;
    }

    public void setIs_my_favorite(boolean is_my_favorite) {
        this.is_my_favorite = is_my_favorite;
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

    public int getAuthed_product_count() {
        return authed_product_count;
    }

    public void setAuthed_product_count(int authed_product_count) {
        this.authed_product_count = authed_product_count;
    }

    public Evaluation getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(Evaluation evaluation) {
        this.evaluation = evaluation;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public Requirement getRequirement() {
        return requirement;
    }

    public void setRequirement(Requirement requirement) {
        this.requirement = requirement;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public String getAuth_message() {
        return auth_message;
    }

    public void setAuth_message(String auth_message) {
        this.auth_message = auth_message;
    }

    public long getAuth_date() {
        return auth_date;
    }

    public void setAuth_date(long auth_date) {
        this.auth_date = auth_date;
    }

    public String getUid_auth_message() {
        return uid_auth_message;
    }

    public void setUid_auth_message(String uid_auth_message) {
        this.uid_auth_message = uid_auth_message;
    }

    public long getUid_auth_date() {
        return uid_auth_date;
    }

    public void setUid_auth_date(long uid_auth_date) {
        this.uid_auth_date = uid_auth_date;
    }

    public String getEmail_auth_type() {
        return email_auth_type;
    }

    public void setEmail_auth_type(String email_auth_type) {
        this.email_auth_type = email_auth_type;
    }

    public String getEmail_auth_message() {
        return email_auth_message;
    }

    public void setEmail_auth_message(String email_auth_message) {
        this.email_auth_message = email_auth_message;
    }

    public long getEmail_auth_date() {
        return email_auth_date;
    }

    public void setEmail_auth_date(long email_auth_date) {
        this.email_auth_date = email_auth_date;
    }

    public String getWork_auth_type() {
        return work_auth_type;
    }

    public void setWork_auth_type(String work_auth_type) {
        this.work_auth_type = work_auth_type;
    }

    public String getWork_auth_message() {
        return work_auth_message;
    }

    public void setWork_auth_message(String work_auth_message) {
        this.work_auth_message = work_auth_message;
    }

    public long getWork_auth_date() {
        return work_auth_date;
    }

    public void setWork_auth_date(long work_auth_date) {
        this.work_auth_date = work_auth_date;
    }

}

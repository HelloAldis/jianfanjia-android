package com.jianfanjia.api.model;

import java.util.List;

/**
 * Created by Aldis on 16/3/28.
 */
public class Requirement extends BaseModel {
    private String _id;
    private String userid;
    private String province;
    private String city;
    private String district;
    private String basic_address;
    private String detail_address;
    private int house_area;
    private String house_type;
    private String dec_style;
    private String dec_type = "0";
    private String work_type;
    private String communication_type;
    private String package_type;
    private String prefer_sex;
    private int total_price;
    private String final_planid;
    private String final_designerid;
    private String business_house_type;
    private Process process;
    private long create_at;
    private long last_status_update_time;
    private long start_at;
    private String family_description;
    private String status;
    private List<String> order_designerids;
    private List<String> rec_designerids;
    private List<Designer> rec_designers;//匹配的设计师
    private List<Designer> order_designers;//预约的设计师
    private Plan plan;
    private Designer designer;
    private User user;
    private Evaluation evaluation;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPackage_type() {
        return package_type;
    }

    public void setPackage_type(String package_type) {
        this.package_type = package_type;
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

    public int getHouse_area() {
        return house_area;
    }

    public void setHouse_area(int house_area) {
        this.house_area = house_area;
    }

    public int getTotal_price() {
        return total_price;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }

    public String getHouse_type() {
        return house_type;
    }

    public void setHouse_type(String house_type) {
        this.house_type = house_type;
    }

    public String getDec_style() {
        return dec_style;
    }

    public void setDec_style(String dec_style) {
        this.dec_style = dec_style;
    }

    public String getDec_type() {
        return dec_type;
    }

    public void setDec_type(String dec_type) {
        this.dec_type = dec_type;
    }

    public String getWork_type() {
        return work_type;
    }

    public void setWork_type(String work_type) {
        this.work_type = work_type;
    }

    public String getCommunication_type() {
        return communication_type;
    }

    public void setCommunication_type(String communication_type) {
        this.communication_type = communication_type;
    }

    public String getPrefer_sex() {
        return prefer_sex;
    }

    public void setPrefer_sex(String prefer_sex) {
        this.prefer_sex = prefer_sex;
    }


    public String getFinal_planid() {
        return final_planid;
    }

    public void setFinal_planid(String final_planid) {
        this.final_planid = final_planid;
    }

    public String getFinal_designerid() {
        return final_designerid;
    }

    public void setFinal_designerid(String final_designerid) {
        this.final_designerid = final_designerid;
    }

    public String getBusiness_house_type() {
        return business_house_type;
    }

    public void setBusiness_house_type(String business_house_type) {
        this.business_house_type = business_house_type;
    }

    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public long getCreate_at() {
        return create_at;
    }

    public void setCreate_at(long create_at) {
        this.create_at = create_at;
    }

    public long getLast_status_update_time() {
        return last_status_update_time;
    }

    public void setLast_status_update_time(long last_status_update_time) {
        this.last_status_update_time = last_status_update_time;
    }

    public String getFamily_description() {
        return family_description;
    }

    public void setFamily_description(String family_description) {
        this.family_description = family_description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getOrder_designerids() {
        return order_designerids;
    }

    public void setOrder_designerids(List<String> order_designerids) {
        this.order_designerids = order_designerids;
    }

    public List<String> getRec_designerids() {
        return rec_designerids;
    }

    public void setRec_designerids(List<String> rec_designerids) {
        this.rec_designerids = rec_designerids;
    }

    public List<Designer> getRec_designers() {
        return rec_designers;
    }

    public void setRec_designers(List<Designer> rec_designers) {
        this.rec_designers = rec_designers;
    }

    public List<Designer> getOrder_designers() {
        return order_designers;
    }

    public void setOrder_designers(List<Designer> order_designers) {
        this.order_designers = order_designers;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
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

    public Evaluation getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(Evaluation evaluation) {
        this.evaluation = evaluation;
    }

    public long getStart_at() {
        return start_at;
    }

    public void setStart_at(long start_at) {
        this.start_at = start_at;
    }

    public String getBasic_address() {
        return basic_address;
    }

    public void setBasic_address(String basic_address) {
        this.basic_address = basic_address;
    }

    public String getDetail_address() {
        return detail_address;
    }

    public void setDetail_address(String detail_address) {
        this.detail_address = detail_address;
    }
}

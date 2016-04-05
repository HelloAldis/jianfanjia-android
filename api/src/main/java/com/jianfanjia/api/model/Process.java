package com.jianfanjia.api.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Aldis on 16/3/28.
 */
public class Process extends BaseModel implements Serializable {
    private static final long serialVersionUID = -4608194481708495533L;
    private String _id;
    private String requirementid;
    private String final_planid;
    private String final_designerid;
    private String province;
    private String city;
    private String district;
    private String dec_type;
    private String basic_address;
    private String detail_address;
    private String house_type;
    private String house_area;
    private String dec_style;
    private String work_type;
    private String total_price;
    private long start_at;
    private long lastupdate;
    private String duration;

    private String userid;

    private User user;

    private String going_on;

    private ArrayList<ProcessSection> sections;

    private Requirement requirement;

    private Plan plan;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getRequirementid() {
        return requirementid;
    }

    public void setRequirementid(String requirementid) {
        this.requirementid = requirementid;
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

    public String getHouse_type() {
        return house_type;
    }

    public void setHouse_type(String house_type) {
        this.house_type = house_type;
    }

    public String getHouse_area() {
        return house_area;
    }

    public void setHouse_area(String house_area) {
        this.house_area = house_area;
    }

    public String getDec_style() {
        return dec_style;
    }

    public void setDec_style(String dec_style) {
        this.dec_style = dec_style;
    }

    public String getWork_type() {
        return work_type;
    }

    public void setWork_type(String work_type) {
        this.work_type = work_type;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public long getStart_at() {
        return start_at;
    }

    public void setStart_at(long start_at) {
        this.start_at = start_at;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getGoing_on() {
        return going_on;
    }

    public void setGoing_on(String going_on) {
        this.going_on = going_on;
    }

    public ArrayList<ProcessSection> getSections() {
        return sections;
    }

    public void setSections(ArrayList<ProcessSection> sections) {
        this.sections = sections;
    }

    public Requirement getRequirement() {
        return requirement;
    }

    public void setRequirement(Requirement requirement) {
        this.requirement = requirement;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public long getLastupdate() {
        return lastupdate;
    }

    public void setLastupdate(long lastupdate) {
        this.lastupdate = lastupdate;
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

    public String getDec_type() {
        return dec_type;
    }

    public void setDec_type(String dec_type) {
        this.dec_type = dec_type;
    }

    public ProcessSection getSectionInfoByName(String sectionName) {
        if (sections != null) {
            for (ProcessSection sectionInfo : sections) {
                if (sectionInfo.getName().equals(sectionName)) {
                    return sectionInfo;
                }
            }
        }
        return null;
    }
}

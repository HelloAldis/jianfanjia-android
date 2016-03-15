package com.jianfanjia.cn.designer.bean;

import java.io.Serializable;

/**
 * @author zhanghao
 * @ClassName: DesignerSiteInfo
 * @Description: 工地基本信息
 * @date 2015-8-26 下午20:03
 */
public class Process implements Serializable {

    private String _id;// 工地id

    private String city;

    private String district;

    private String cell;// 小区名称

    private String userid;// 业主id

    private String going_on;// 所处阶段

    private String final_designerid;//设计师id

    private String final_planid;

    private String requirementid;

    private long start_at;

    private long lastupdate;

    private User user;

    private PlanInfo plan;

    private RequirementInfo requirement;

    public String getFinal_designerid() {
        return final_designerid;
    }

    public void setFinal_designerid(String final_designerid) {
        this.final_designerid = final_designerid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCell() {
        return cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getGoing_on() {
        return going_on;
    }

    public void setGoing_on(String going_on) {
        this.going_on = going_on;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getFinal_planid() {
        return final_planid;
    }

    public void setFinal_planid(String final_planid) {
        this.final_planid = final_planid;
    }

    public String getRequirementid() {
        return requirementid;
    }

    public void setRequirementid(String requirementid) {
        this.requirementid = requirementid;
    }

    public long getLastupdate() {
        return lastupdate;
    }

    public void setLastupdate(long lastupdate) {
        this.lastupdate = lastupdate;
    }

    public PlanInfo getPlan() {
        return plan;
    }

    public void setPlan(PlanInfo plan) {
        this.plan = plan;
    }

    public long getStart_at() {
        return start_at;
    }

    public void setStart_at(long start_at) {
        this.start_at = start_at;
    }

    public RequirementInfo getRequirement() {
        return requirement;
    }

    public void setRequirement(RequirementInfo requirement) {
        this.requirement = requirement;
    }
}
package com.jianfanjia.cn.designer.bean;

import java.io.Serializable;
import java.util.List;

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

    private Requirement requirement;


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

    public Requirement getRequirement() {
        return requirement;
    }

    public void setRequirement(Requirement requirement) {
        this.requirement = requirement;
    }


    public static class Requirement {
        private String _id;
        private long last_status_update_time;
        private long create_at;
        private String province;
        private String city;
        private String district;
        private String cell;
        private String cell_phase;
        private String cell_building;
        private String cell_unit;
        private String cell_detail_number;
        private String house_type;
        private String dec_style;
        private String dec_type;
        private String prefer_sex;
        private String work_type;
        private String family_description;
        private String userid;
        private int __v;
        private String final_planid;
        private String final_designerid;
        private long start_at;
        private String status;
        private String communication_type;
        private List<String> obsolete_designerids;
        private List<String> order_designerids;
        private List<String> rec_designerids;
        private float total_price;
        private float house_area;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public long getLast_status_update_time() {
            return last_status_update_time;
        }

        public void setLast_status_update_time(long last_status_update_time) {
            this.last_status_update_time = last_status_update_time;
        }

        public long getCreate_at() {
            return create_at;
        }

        public void setCreate_at(long create_at) {
            this.create_at = create_at;
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

        public String getCell() {
            return cell;
        }

        public void setCell(String cell) {
            this.cell = cell;
        }

        public String getCell_phase() {
            return cell_phase;
        }

        public void setCell_phase(String cell_phase) {
            this.cell_phase = cell_phase;
        }

        public String getCell_building() {
            return cell_building;
        }

        public void setCell_building(String cell_building) {
            this.cell_building = cell_building;
        }

        public String getCell_unit() {
            return cell_unit;
        }

        public void setCell_unit(String cell_unit) {
            this.cell_unit = cell_unit;
        }

        public String getCell_detail_number() {
            return cell_detail_number;
        }

        public void setCell_detail_number(String cell_detail_number) {
            this.cell_detail_number = cell_detail_number;
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

        public String getPrefer_sex() {
            return prefer_sex;
        }

        public void setPrefer_sex(String prefer_sex) {
            this.prefer_sex = prefer_sex;
        }

        public String getWork_type() {
            return work_type;
        }

        public void setWork_type(String work_type) {
            this.work_type = work_type;
        }

        public String getFamily_description() {
            return family_description;
        }

        public void setFamily_description(String family_description) {
            this.family_description = family_description;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public int get__v() {
            return __v;
        }

        public void set__v(int __v) {
            this.__v = __v;
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

        public long getStart_at() {
            return start_at;
        }

        public void setStart_at(long start_at) {
            this.start_at = start_at;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCommunication_type() {
            return communication_type;
        }

        public void setCommunication_type(String communication_type) {
            this.communication_type = communication_type;
        }

        public List<String> getObsolete_designerids() {
            return obsolete_designerids;
        }

        public void setObsolete_designerids(List<String> obsolete_designerids) {
            this.obsolete_designerids = obsolete_designerids;
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

        public float getTotal_price() {
            return total_price;
        }

        public void setTotal_price(float total_price) {
            this.total_price = total_price;
        }

        public float getHouse_area() {
            return house_area;
        }

        public void setHouse_area(float house_area) {
            this.house_area = house_area;
        }
    }
}

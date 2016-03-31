package com.jianfanjia.api.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Aldis on 16/3/28.
 */
public class Requirement extends BaseModel implements Serializable {
    private static final long serialVersionUID = -5082792601841753217L;

    private String _id;
    private String userid;
    private String province;
    private String city;
    private String district;
    private String street;
    private String cell_phase;
    private String cell_building;
    private String cell_unit;
    private String cell_detail_number;
    private String cell;
    private String house_area;
    private String house_type;
    private String dec_style;
    private String dec_type;
    private String work_type;
    private String communication_type;
    private String prefer_sex;
    private String total_price;
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

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
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

    public String getCell() {
        return cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

    public String getHouse_area() {
        return house_area;
    }

    public void setHouse_area(String house_area) {
        this.house_area = house_area;
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

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
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
}

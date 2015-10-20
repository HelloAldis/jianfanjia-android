package com.jianfanjia.cn.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhanghao
 * @class RequirementInfo
 * @Description 此类是需求信息实体类
 * @date 2015-8-28 10:05
 */
public class RequirementInfo implements Serializable {
    private static final long serialVersionUID = 1L;
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
    private String house_type;
    private String house_area;
    private String dec_style;
    private String work_type;
    private String total_price;
    private long create_at;
    private String family_description;
    private String status;
    private String communication_type;
    private List<DesignerInfo> order_designerids;
    private List<DesignerInfo> rec_designerids;

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

    public long getCreate_at() {
        return create_at;
    }

    public void setCreate_at(long create_at) {
        this.create_at = create_at;
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

    public String getCommunication_type() {
        return communication_type;
    }

    public void setCommunication_type(String communication_type) {
        this.communication_type = communication_type;
    }

    public List<DesignerInfo> getOrder_designerids() {
        return order_designerids;
    }

    public void setOrder_designerids(List<DesignerInfo> order_designerids) {
        this.order_designerids = order_designerids;
    }

    public List<DesignerInfo> getRec_designerids() {
        return rec_designerids;
    }

    public void setRec_designerids(List<DesignerInfo> rec_designerids) {
        this.rec_designerids = rec_designerids;
    }

    public String getCell_phase() {
        return cell_phase;
    }

    public void setCell_phase(String cell_phase) {
        this.cell_phase = cell_phase;
    }
}

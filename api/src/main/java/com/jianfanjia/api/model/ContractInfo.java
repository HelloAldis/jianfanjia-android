package com.jianfanjia.api.model;

import java.io.Serializable;
import java.util.List;

/**
 * Name: ContractInfo
 * User: fengliang
 * Date: 2015-10-26
 * Time: 14:47
 */
public class ContractInfo implements Serializable {
    private static final long serialVersionUID = -5936540477117400623L;
    private String _id;
    private long last_status_update_time;
    private long create_at;
    private String province;
    private String city;
    private String district;
    private String cell;
    private String street;
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
    private List<String> order_designerids;
    private List<String> rec_designerids;
    private float total_price;
    private int house_area;
    private Plan plan;
    private Designer designer;
    private User user;


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

    public int getHouse_area() {
        return house_area;
    }

    public void setHouse_area(int house_area) {
        this.house_area = house_area;
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

    public static class Plan {
        private String _id;
        private int duration;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }
    }

    public static class Designer {
        private String _id;
        private String phone;
        private String district;
        private String city;
        private String province;
        private String username;
        private String imageid;
        private String work_auth_type;
        private String email_auth_type;
        private String uid_auth_type;
        private String auth_type;

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

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
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

        public String getWork_auth_type() {
            return work_auth_type;
        }

        public void setWork_auth_type(String work_auth_type) {
            this.work_auth_type = work_auth_type;
        }

        public String getEmail_auth_type() {
            return email_auth_type;
        }

        public void setEmail_auth_type(String email_auth_type) {
            this.email_auth_type = email_auth_type;
        }

        public String getUid_auth_type() {
            return uid_auth_type;
        }

        public void setUid_auth_type(String uid_auth_type) {
            this.uid_auth_type = uid_auth_type;
        }

        public String getAuth_type() {
            return auth_type;
        }

        public void setAuth_type(String auth_type) {
            this.auth_type = auth_type;
        }
    }

    public static class User {
        private String _id;
        private String phone;
        private String username;

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

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}

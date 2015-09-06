package com.jianfanjia.cn.bean;

import java.io.Serializable;

public class Requirement implements Serializable {
	private static final long serialVersionUID = 1529240298901889207L;
	private String _id;

	private String userid;

	private String work_type;

	private String dec_style;

	private String house_type;

	private String cell;

	private String district;

	private String city;

	private String __v;

	private String province;

	private String final_designerid;

	private String communication_type;

	private String[] rec_designerids;

	private String[] designerids;

	private String total_price;

	private String house_area;

	public String getHouse_type() {
		return house_type;
	}

	public void setHouse_type(String house_type) {
		this.house_type = house_type;
	}

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

	public String getWork_type() {
		return work_type;
	}

	public void setWork_type(String work_type) {
		this.work_type = work_type;
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

	public String get__v() {
		return __v;
	}

	public void set__v(String __v) {
		this.__v = __v;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getFinal_designerid() {
		return final_designerid;
	}

	public void setFinal_designerid(String final_designerid) {
		this.final_designerid = final_designerid;
	}

	public String getCommunication_type() {
		return communication_type;
	}

	public void setCommunication_type(String communication_type) {
		this.communication_type = communication_type;
	}

	public String[] getRec_designerids() {
		return rec_designerids;
	}

	public void setRec_designerids(String[] rec_designerids) {
		this.rec_designerids = rec_designerids;
	}

	public String[] getDesignerids() {
		return designerids;
	}

	public void setDesignerids(String[] designerids) {
		this.designerids = designerids;
	}

	public String getTotal_price() {
		return total_price;
	}

	public void setTotal_price(String total_price) {
		this.total_price = total_price;
	}

	public String getHouse_area() {
		return house_area;
	}

	public void setHouse_area(String house_area) {
		this.house_area = house_area;
	}
}

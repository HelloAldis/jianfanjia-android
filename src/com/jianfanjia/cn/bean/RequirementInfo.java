package com.jianfanjia.cn.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * @class RequirementInfo
 * @Description 此类是需求信息实体类
 * @author zhanghao
 * @date 2015-8-28 10:05
 * 
 */
public class RequirementInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private String final_designerid;
	private String province;
	private String city;
	private String district;
	private String cell;
	private String house_type;
	private String house_area;
	private String dec_style;
	private String work_type;
	private String total_price;
	private Date start_at;
	private String duration;

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

	public String getCell() {
		return cell;
	}

	public void setCell(String cell) {
		this.cell = cell;
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

	public Date getStart_at() {
		return start_at;
	}

	public void setStart_at(Date start_at) {
		this.start_at = start_at;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	@Override
	public String toString() {
		return super.toString();
	}

}

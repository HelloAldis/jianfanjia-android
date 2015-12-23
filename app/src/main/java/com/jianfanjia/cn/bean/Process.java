package com.jianfanjia.cn.bean;

import java.io.Serializable;

/**
 *
 * @ClassName: DesignerSiteInfo
 * @Description: 工地基本信息
 * @author zhanghao
 * @date 2015-8-26 下午20:03
 *
 */
public class Process implements Serializable {

	private static final long serialVersionUID = -5635939585005504702L;
	private String _id;// 工地id

	private String city;

	private String district;

	private String cell;// 小区名称

	private String userid;// 业主id

	private String going_on;// 所处阶段

	private String final_designerid;//设计师id

	private User user;

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

}

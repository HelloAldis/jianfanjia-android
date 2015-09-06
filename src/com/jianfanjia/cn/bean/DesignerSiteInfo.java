package com.jianfanjia.cn.bean;

import java.io.Serializable;

/**
 * 
 * @ClassName: DesignerSiteInfo
 * @Description: 我的工地信息类（设计师）
 * @author zhanghao
 * @date 2015-8-26 下午20:03
 * 
 */
public class DesignerSiteInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	private String userid;// 设计师id

	private String goingon;// 所处阶段

	private String district;

	private String siteid;// 工地id

	private String cell;// 小区名称

	private String city;

	private OwnerInfo info;

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

	public String getSiteid() {
		return siteid;
	}

	public void setSiteid(String siteid) {
		this.siteid = siteid;
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

	public OwnerInfo getInfo() {
		return info;
	}

	public void setInfo(OwnerInfo info) {
		this.info = info;
	}

	public String getGoingon() {
		return goingon;
	}

	public void setGoingon(String goingon) {
		this.goingon = goingon;
	}

}

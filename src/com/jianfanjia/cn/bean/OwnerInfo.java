package com.jianfanjia.cn.bean;

import java.io.Serializable;

public class OwnerInfo implements Serializable {
	private static final long serialVersionUID = -1450173592300692043L;

	private String ownerid;

	private String imageid;

	private String phone;

	private String name;

	public String getOwnerid() {
		return ownerid;
	}

	public void setOwnerid(String ownerid) {
		this.ownerid = ownerid;
	}

	public String getImageid() {
		return imageid;
	}

	public void setImageid(String imageid) {
		this.imageid = imageid;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}

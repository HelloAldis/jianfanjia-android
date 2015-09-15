package com.jianfanjia.cn.bean;

import java.io.Serializable;

public class DesignerUpdateInfo implements Serializable {
	private static final long serialVersionUID = 8059290088434087451L;

	private String philosophy;

	private String achievement;

	private String design_fee_range;

	private String company;

	private String uid;

	private String address;

	private String district;

	private String city;

	private String sex;

	private String username;

	private int dec_fee_all;

	private int dec_fee_half;

	private String[] dec_districts;

	private String[] dec_styles;

	private String[] dec_types;

	public String getPhilosophy() {
		return philosophy;
	}

	public void setPhilosophy(String philosophy) {
		this.philosophy = philosophy;
	}

	public String getAchievement() {
		return achievement;
	}

	public void setAchievement(String achievement) {
		this.achievement = achievement;
	}

	public String getDesign_fee_range() {
		return design_fee_range;
	}

	public void setDesign_fee_range(String design_fee_range) {
		this.design_fee_range = design_fee_range;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getDec_fee_all() {
		return dec_fee_all;
	}

	public void setDec_fee_all(int dec_fee_all) {
		this.dec_fee_all = dec_fee_all;
	}

	public int getDec_fee_half() {
		return dec_fee_half;
	}

	public void setDec_fee_half(int dec_fee_half) {
		this.dec_fee_half = dec_fee_half;
	}

	public String[] getDec_districts() {
		return dec_districts;
	}

	public void setDec_districts(String[] dec_districts) {
		this.dec_districts = dec_districts;
	}

	public String[] getDec_styles() {
		return dec_styles;
	}

	public void setDec_styles(String[] dec_styles) {
		this.dec_styles = dec_styles;
	}

	public String[] getDec_types() {
		return dec_types;
	}

	public void setDec_types(String[] dec_types) {
		this.dec_types = dec_types;
	}
	
	

}

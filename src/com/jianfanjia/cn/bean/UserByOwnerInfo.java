package com.jianfanjia.cn.bean;

import java.io.Serializable;

/**
 * 
 * @ClassName: UserByOwnerInfo
 * @Description:个人信息(业主)
 * @author fengliang
 * @date 2015-8-18 下午1:37:58
 * 
 */
public class UserByOwnerInfo implements Serializable {
	private static final long serialVersionUID = 8954526830558571075L;
	private String _id;

	private String accessToken;

	private String pass;// 用户密码

	private String phone;// 用户电话，用来做key,每个用户一个电话

	private String communication_type;

	private int __v;

	private String imageId;// 用户头像

	private String address;// 用户头像

	private String district;

	private String city;// 用户所在城市

	private String province;

	private String sex;// 我的性别

	private String username;// 用户姓名

	private boolean is_block;

	private int score;

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	@Override
	public String toString() {
		return null;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getCommunication_type() {
		return communication_type;
	}

	public void setCommunication_type(String communication_type) {
		this.communication_type = communication_type;
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

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public boolean isIs_block() {
		return is_block;
	}

	public void setIs_block(boolean is_block) {
		this.is_block = is_block;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int get__v() {
		return __v;
	}

	public void set__v(int __v) {
		this.__v = __v;
	}

}

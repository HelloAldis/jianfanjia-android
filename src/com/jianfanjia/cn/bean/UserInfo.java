package com.jianfanjia.cn.bean;

import java.io.Serializable;

/**
 * 
 * @ClassName: UserInfo
 * @Description: TODO
 * @author fengliang
 * @date 2015-8-18 下午1:37:58
 * 
 */
public class UserInfo implements Serializable {
	private static final long serialVersionUID = 8954526830558571075L;

	private String username;// 用户姓名

	private String phone;// 用户电话，用来做key,每个用户一个电话

	private String pass;// 用户密码

	private String city;// 用户所在城市

	private String imageId;// 用户头像

	private String usertype;// 用户身份，用来标识设计师还是业主

	private String sex;// 我的性别

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

	public String getUsertype() {
		return usertype;
	}

	public void setUsertype(String usertype) {
		this.usertype = usertype;
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

}

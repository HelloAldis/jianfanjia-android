package com.jianfanjia.cn.bean;

import java.io.Serializable;

/**
 * 
 * @ClassName: LoginUserBean
 * @Description: µÇÂ¼ÓÃ»§
 * @author fengliang
 * @date 2015-8-25 ÉÏÎç11:33:32
 * 
 */
public class LoginUserBean implements Serializable {
	private static final long serialVersionUID = 7912343352449337196L;
	private String username;
	private String usertype;
	private String phone;
	private String pass;
	private String imageId;
	private String id;

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsertype() {
		return usertype;
	}

	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
}

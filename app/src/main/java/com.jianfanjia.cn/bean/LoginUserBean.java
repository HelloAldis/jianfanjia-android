package com.jianfanjia.cn.bean;

import java.io.Serializable;

/**
 * 
 * @ClassName: LoginUserBean
 * @Description: 登录用户
 * @author fengliang
 * @date 2015-8-25 上午11:33:32
 * 
 */
public class LoginUserBean implements Serializable {
	private static final long serialVersionUID = 7912343352449337196L;
	private String username;
	private String usertype;
	private String phone;
	private String pass;
	private String imageid;
	private String _id;

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getImageid() {
		return imageid;
	}

	public void setImageid(String imageid) {
		this.imageid = imageid;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
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

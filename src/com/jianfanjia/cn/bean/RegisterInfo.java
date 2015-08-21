package com.jianfanjia.cn.bean;

import java.io.Serializable;

import com.jianfanjia.cn.tools.LogTool;

/**
 * @version 1.0
 * @Decription 此类是一个注册实体类
 * @author zhanghao
 * 
 * 
 */
public class RegisterInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String phone;// 手机号

	private String pass;// 密码

	private String code;// 验证码

	private String userType;// 用户类型

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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	@Override
	public String toString() {
		String regJson = "{\"phone\" :" + phone + "," + "\"pass\" :" + pass + ","
				+ "\"code\" :" + code + "," + "\"type\" :" + userType + "}";
		LogTool.d(this.getClass().getName(), regJson);
		return regJson;
	}

}

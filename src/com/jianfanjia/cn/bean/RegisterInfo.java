package com.jianfanjia.cn.bean;

import java.io.Serializable;
import com.jianfanjia.cn.tools.LogTool;

/**
 * @version 1.0
 * @Decription ������һ��ע��ʵ����
 * @author zhanghao
 * 
 * 
 */
public class RegisterInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private String phone;// �ֻ���
	private String pass;// ����
	private String code;// ��֤��
	private String type;// �û�����

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		String regJson = "{\"phone\" : \"" + phone + "\"," + "\"pass\" : \""
				+ pass + "\"," + "\"code\" : \"" + code + "\","
				+ "\"type\" : \"" + type + "\"}";
		LogTool.d(this.getClass().getName(), regJson);
		return regJson;
	}
}

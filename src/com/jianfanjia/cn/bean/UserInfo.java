package com.jianfanjia.cn.bean;

import java.io.Serializable;

/**
 * 
 * @ClassName: UserInfo
 * @Description: TODO
 * @author fengliang
 * @date 2015-8-18 ����1:37:58
 * 
 */
public class UserInfo implements Serializable {
	private static final long serialVersionUID = 8954526830558571075L;

	private String username;// �û�����

	private String phone;// �û��绰��������key,ÿ���û�һ���绰

	private String pass;// �û�����

	private String city;// �û����ڳ���

	private String imageId;// �û�ͷ��

	private String usertype;// �û���ݣ�������ʶ���ʦ����ҵ��

	private String sex;// �ҵ��Ա�

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

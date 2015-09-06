package com.jianfanjia.cn.bean;

import java.io.Serializable;

/**
 * 
 * @ClassName: DesignerSiteInfo
 * @Description: �ҵĹ�����Ϣ�ࣨ���ʦ��
 * @author zhanghao
 * @date 2015-8-26 ����20:03
 * 
 */
public class DesignerSiteInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	private String _id;// ����id

	private String city;

	private String district;

	private String cell;// С������

	private String userid;// ���ʦid

	private String going_on;// �����׶�

	private User user;

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

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getGoing_on() {
		return going_on;
	}

	public void setGoing_on(String going_on) {
		this.going_on = going_on;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}

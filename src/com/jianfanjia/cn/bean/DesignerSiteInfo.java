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

	private String userid;// ���ʦid

	private String goingon;// �����׶�

	private String district;

	private String siteid;// ����id

	private String cell;// С������

	private String city;

	private OwnerInfo info;

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

	public String getSiteid() {
		return siteid;
	}

	public void setSiteid(String siteid) {
		this.siteid = siteid;
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

	public OwnerInfo getInfo() {
		return info;
	}

	public void setInfo(OwnerInfo info) {
		this.info = info;
	}

	public String getGoingon() {
		return goingon;
	}

	public void setGoingon(String goingon) {
		this.goingon = goingon;
	}

}

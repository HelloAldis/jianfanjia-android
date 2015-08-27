package com.jianfanjia.cn.bean;

import java.io.Serializable;

/**
 * 
 * @version 1.0
 * @Description: �����ǹ���ʵ����
 * @author zhanghao
 * @date 2015-8-19 ����4:20:58
 * 
 */
public class OwnerSiteInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String userPhone;// ҵ���绰

	private String designerPhone;// ���ʦ�绰

	private String city;// ���ڳ���

	private String district;// ������

	private String villageName;// С������

	private String houseStyle;// ����

	private String houseSize;// �������

	private String loveStyle;// ���ϲ��

	private String decorationStyle;// װ������

	private String decorationBudget;// װ��Ԥ��

	private String startDate;// ��������

	private int totalDate;// �ܹ���

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getDesignerPhone() {
		return designerPhone;
	}

	public void setDesignerPhone(String designerPhone) {
		this.designerPhone = designerPhone;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getVillageName() {
		return villageName;
	}

	public void setVillageName(String villageName) {
		this.villageName = villageName;
	}

	public String getHouseStyle() {
		return houseStyle;
	}

	public void setHouseStyle(String houseStyle) {
		this.houseStyle = houseStyle;
	}

	public String getHouseSize() {
		return houseSize;
	}

	public void setHouseSize(String houseSize) {
		this.houseSize = houseSize;
	}

	public String getLoveStyle() {
		return loveStyle;
	}

	public void setLoveStyle(String loveStyle) {
		this.loveStyle = loveStyle;
	}

	public String getDecorationStyle() {
		return decorationStyle;
	}

	public void setDecorationStyle(String decorationStyle) {
		this.decorationStyle = decorationStyle;
	}

	public String getDecorationBudget() {
		return decorationBudget;
	}

	public void setDecorationBudget(String decorationBudget) {
		this.decorationBudget = decorationBudget;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public int getTotalDate() {
		return totalDate;
	}

	public void setTotalDate(int totalDate) {
		this.totalDate = totalDate;
	}

}

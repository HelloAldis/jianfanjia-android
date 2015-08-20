package com.jianfanjia.cn.bean;

import java.io.Serializable;
import java.util.ArrayList;

import android.content.Context;

/**
 * 
 * @version 1.0
 * @Description: �����ǹ���ʵ����
 * @author zhanghao
 * @date 2015-8-19 ����4:20:58
 * 
 */
public class SiteInfo implements Serializable {
	
	private String userPhone;//业主电话
	
	private String designerPhone;//设计师电话

	private String city;//所在城市
	
	private String district;//所在区
	
	private String villageName;//小区名称
	
	private String houseStyle;//户型
	
	private String houseSize;//房子面积
	
	private String loveStyle;//风格喜好
	
	private String decorationStyle;//装修类型
	
	private String decorationBudget;//装修预算
	
	private String startDate;//开工日期
	
	private int totalDate;//总工期 
	
	private int currentPro;//当前所在工序
	
	private ArrayList<ProcedureInfo> procedures = new ArrayList<ProcedureInfo>();//�?包含的所有工�?
	
	private Context context;
	
	public SiteInfo(Context context){
		this.context = context;
	}
	
	
	public String getDesignerPhone() {
		return designerPhone;
	}

	public void setDesignerPhone(String designerPhone) {
		this.designerPhone = designerPhone;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public int getCurrentPro() {
		return currentPro;
	}

	public void setCurrentPro(int currentPro) {
		this.currentPro = currentPro;
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


	public ArrayList<ProcedureInfo> getProcedures() {
		return procedures;
	}

	public void setProcedures(ArrayList<ProcedureInfo> procedures) {
		this.procedures = procedures;
	}

	@Override
	public String toString() {
		return super.toString();
	}
	

}

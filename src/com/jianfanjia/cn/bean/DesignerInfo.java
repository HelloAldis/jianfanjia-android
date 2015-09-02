package com.jianfanjia.cn.bean;

import java.util.ArrayList;

/**
 * 
 * 
 * @ClassName: DesignerInfo
 * @Description: TODO
 * @author zhanghao
 * @date 2015-9-2 11:52
 * 
 */
public class DesignerInfo extends LoginUserBean {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String big_imageid;
	
	private String address;
	
	private String district;
	
	private String city;
	
	private String sex;
	
	private String order_count;
	
	private String product_count;
	
	private String province;
	
	private ArrayList<String> dec_styles;
	
	private String auth_type;//ÈÏÖ¤×´Ì¬
	
	private String design_fee_range;
	
	public String getDesign_fee_range() {
		return design_fee_range;
	}

	public void setDesign_fee_range(String design_fee_range) {
		this.design_fee_range = design_fee_range;
	}

	public String getAuth_type() {
		return auth_type;
	}

	public void setAuth_type(String auth_type) {
		this.auth_type = auth_type;
	}

	public String getBig_imageid() {
		return big_imageid;
	}

	public void setBig_imageid(String big_imageid) {
		this.big_imageid = big_imageid;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getOrder_count() {
		return order_count;
	}

	public void setOrder_count(String order_count) {
		this.order_count = order_count;
	}

	public String getProduct_count() {
		return product_count;
	}

	public void setProduct_count(String product_count) {
		this.product_count = product_count;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public ArrayList<String> getDec_styles() {
		return dec_styles;
	}

	public void setDec_styles(ArrayList<String> dec_styles) {
		this.dec_styles = dec_styles;
	}
}

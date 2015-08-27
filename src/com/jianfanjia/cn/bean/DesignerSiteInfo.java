package com.jianfanjia.cn.bean;

import java.io.Serializable;

/**
 * 
 * @ClassName: DesignerSiteInfo
 * @Description: 我的工地信息类（设计师）
 * @author zhanghao
 * @date 2015-8-26 下午20:03
 * 
 */
public class DesignerSiteInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	private String imageUrl;// 业主头像

	private String name;// 业主姓名

	private String villageName;// 小区名称

	private String address;// 工地地址

	private String stage;// 所处阶段

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

	public String getVillageName() {
		return villageName;
	}

	public void setVillageName(String villageName) {
		this.villageName = villageName;
	}

}

package com.jianfanjia.cn.bean;

import java.io.Serializable;

/**
 * 
 * @ClassName: MyOwerInfo
 * @Description: �ҵ�ҵ����Ϣ�ࣨ���ʦ��
 * @author zhanghao
 * @date 2015-8-26 ����19:33
 * 
 */
public class MyOwnerInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	private String imageUrl;// ҵ��ͷ��

	private String name;// ҵ������

	private String address;// ���ص�ַ

	private String stage;// �����׶�

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

}

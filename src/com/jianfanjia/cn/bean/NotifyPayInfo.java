package com.jianfanjia.cn.bean;

import java.io.Serializable;
/**
 * @Class NotifyPayInfo.class
 * @Decription ��������ʵ����
 * @author zhanghao
 * @date 2015-8-26 ����17:31
 *
 */
public class NotifyPayInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String title;//���ѱ���
	
	private String time;//����ʱ��
	
	private String stage;//���ѽ׶�

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

}

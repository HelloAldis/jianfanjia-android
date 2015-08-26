package com.jianfanjia.cn.bean;

import java.io.Serializable;
/**
 * @Class NotifyPayInfo.class
 * @Decription 付款提醒实体类
 * @author zhanghao
 * @date 2015-8-26 下午17:31
 *
 */
public class NotifyPayInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String title;//提醒标题
	
	private String time;//提醒时间
	
	private String stage;//提醒阶段

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

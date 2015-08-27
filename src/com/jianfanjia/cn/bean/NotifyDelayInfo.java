package com.jianfanjia.cn.bean;

import java.io.Serializable;

/**
 * @Class NotifyDelayInfo.class
 * @Decription 延期提醒实体类
 * @author zhanghao
 * @date 2015-8-26 下午18:00
 * 
 */
public class NotifyDelayInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final int AGREE = 1;// 同意

	public static final int DISAGREE = 2;// 不同意

	private String title;// 提醒标题

	private String content;// 提醒内容

	private String time;// 提醒时间

	private String stage;// 提醒阶段

	private int isagree;// 同意状态

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

	public int getIsagree() {
		return isagree;
	}

	public void setIsagree(int isagree) {
		this.isagree = isagree;
	}

}

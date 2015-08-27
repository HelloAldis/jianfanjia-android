package com.jianfanjia.cn.bean;

import java.io.Serializable;

/**
 * @Class NotifyDelayInfo.class
 * @Decription ��������ʵ����
 * @author zhanghao
 * @date 2015-8-26 ����18:00
 * 
 */
public class NotifyDelayInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final int AGREE = 1;// ͬ��

	public static final int DISAGREE = 2;// ��ͬ��

	private String title;// ���ѱ���

	private String content;// ��������

	private String time;// ����ʱ��

	private String stage;// ���ѽ׶�

	private int isagree;// ͬ��״̬

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

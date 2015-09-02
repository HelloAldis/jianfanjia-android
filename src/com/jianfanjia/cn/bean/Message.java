package com.jianfanjia.cn.bean;

import java.io.Serializable;

/**
 * 
 * @ClassName: Message
 * @Description: 推送消息
 * @author fengliang
 * @date 2015-9-2 上午9:58:19
 * 
 */
public class Message implements Serializable {
	private static final long serialVersionUID = -1386644478824610283L;
	private String msgType;
	private String msgContent;
	private String date;

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}

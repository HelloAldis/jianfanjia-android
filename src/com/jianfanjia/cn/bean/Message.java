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
	private String type;
	private String content;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}

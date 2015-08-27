package com.jianfanjia.cn.bean;

import java.io.Serializable;

/**
 * 
 * @ClassName: CommentInfo
 * @Description: 评论
 * @author fengliang
 * @date 2015-8-25 上午11:34:20
 * 
 */
public class CommentInfo implements Serializable {
	
	private static final long serialVersionUID = 6340039239493931968L;
	
	private String userImageUrl;//评论人头像
	
	private String userName;//评论人姓名
	
	private String content;//评论内容
	
	private String userIdentity;//评论人身份
	
	private String time;//评论时间

	public String getUserImageUrl() {
		return userImageUrl;
	}

	public void setUserImageUrl(String userImageUrl) {
		this.userImageUrl = userImageUrl;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUserIdentity() {
		return userIdentity;
	}

	public void setUserIdentity(String userIdentity) {
		this.userIdentity = userIdentity;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
}

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

	private String id;// 评论id
	
	private String by;//评论人id

	private String content;// 评论内容

	private String usertype;// 评论人身份
	
	private String userName;//评论人姓名
	
	private String userImageUrl;//评论人头像url
	
	private long date;// 评论时间

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBy() {
		return by;
	}

	public void setBy(String by) {
		this.by = by;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUsertype() {
		return usertype;
	}

	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserImageUrl() {
		return userImageUrl;
	}

	public void setUserImageUrl(String userImageUrl) {
		this.userImageUrl = userImageUrl;
	}

	
	
	


}

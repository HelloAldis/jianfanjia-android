package com.jianfanjia.cn.bean;

import java.io.Serializable;
import java.util.ArrayList;

import com.jianfanjia.cn.tools.LogTool;

/**
 * @class SectionItemInfo.class
 * @author zhanghao
 * @Decription 此类是节点信息实体类
 * @date 2015-8-31 上午11:57
 * 
 */
public class SectionItemInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private boolean isOpen;

	private String _id;

	private String name;

	private String status;
	
	private long date;

	private ArrayList<CommentInfo> comments;

	private ArrayList<String> images;

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ArrayList<CommentInfo> getComments() {
		return comments;
	}

	public void setComments(ArrayList<CommentInfo> comments) {
		this.comments = comments;
	}

	public ArrayList<String> getImages() {
		return images;
	}

	public void setImages(ArrayList<String> images) {
		this.images = images;
	}

	public boolean isOpen() {
		return isOpen;
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}
	
	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public void addImageToItem(String imageId){
		LogTool.d("addImage", imageId);
		if(images != null){
			images.add(imageId);
		}
	}
	
	public void addCommentToItem(CommentInfo commentInfo){
		if(comments != null){
			comments.add(commentInfo);
		}
	}
	
}

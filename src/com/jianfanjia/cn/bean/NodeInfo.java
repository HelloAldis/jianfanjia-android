package com.jianfanjia.cn.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.net.Uri;

/**
 * @version 1.0
 * @Description 此类是节点实体类
 * @author zhanghao
 * @date 2015-8-19 15:15:20
 * 
 */
public class NodeInfo implements Serializable {

	private static final long serialVersionUID = -7787432251105107090L;

	public static final int NOT_START = 0x00;// 未施工?
	public static final int WORKING = 0x01;// 正在施工
	public static final int FINISH = 0x02;// 已完工?

	public static final int WORKING_TYPE = 0x03;// 施工类型
	public static final int CHECK_TYPE = 0x04;// 验证类型

	private String nodeName;// 当前节点的名字?
	private int finishStatus;// 该节点是否完工?

	private List<Uri> imageUris = new ArrayList<Uri>();// 当前节点要上传的图片
	private List<CommentInfo> comment = new ArrayList<CommentInfo>();// 评论

	public int getFinishStatus() {
		return finishStatus;
	}

	public void setFinishStatus(int finishStatus) {
		this.finishStatus = finishStatus;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public List<Uri> getImageUris() {
		return imageUris;
	}

	public void setImageUris(List<Uri> imageUris) {
		this.imageUris = imageUris;
	}

	public List<CommentInfo> getComment() {
		return comment;
	}

	public void setComment(List<CommentInfo> comment) {
		this.comment = comment;
	}
}

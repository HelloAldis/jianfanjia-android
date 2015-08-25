package com.jianfanjia.cn.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.net.Uri;

/**
 * @version 1.0
 * @Description 姝ょ被鏄妭鐐瑰疄浣撶被
 * @author zhanghao
 * @date 2015-8-19 15:15:20
 * 
 */
public class NodeInfo implements Serializable {
	private static final long serialVersionUID = -7787432251105107090L;
	public static final int NOT_START = 0x00;// 鏈柦宸�?
	public static final int WORKING = 0x01;// 姝ｅ湪鏂藉伐
	public static final int FINISH = 0x02;// 宸插畬宸�?
	public static final int WORKING_TYPE = 0x03;// 鏂藉伐绫诲瀷
	public static final int CHECK_TYPE = 0x04;// 楠岃瘉绫诲瀷

	private String nodeName;// 褰撳墠鑺傜偣鐨勫悕瀛�?
	private int finishStatus;// 璇ヨ妭鐐规槸鍚﹀畬宸�?

	private List<Uri> imageUris = new ArrayList<Uri>();// 褰撳墠鑺傜偣瑕佷笂浼犵殑鍥剧墖
	private List<CommentInfo> comment = new ArrayList<CommentInfo>();// 璇勮

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

package com.jianfanjia.cn.bean;

import java.util.ArrayList;

/**
 * @class ProcessInfo
 * @Decription 此类是工地信息类
 * @author zhanghao
 * @date 2015-8-28 15:30
 * 
 */
public class ProcessInfo extends RequirementInfo {
	private static final long serialVersionUID = 1L;

	private String _id;

	private String userid;

	private String going_on;

	private ArrayList<SectionInfo> sections;

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getGoing_on() {
		return going_on;
	}

	public void setGoing_on(String going_on) {
		this.going_on = going_on;
	}

	public ArrayList<SectionInfo> getSections() {
		return sections;
	}

	public void setSections(ArrayList<SectionInfo> sections) {
		this.sections = sections;
	}

}

package com.jianfanjia.cn.bean;

import java.io.Serializable;

/**
 * 
 * @ClassName: MyOwerInfo
 * @Description: 我的业主信息类（设计师）
 * @author zhanghao
 * @date 2015-8-26 下午19:33
 * 
 */
public class MyOwnerInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	private String designerid;

	private String userid;

	private String requirementid;

	private String status;

	private String _id;

	private User user;

	private Requirement requirement;

	public String getDesignerid() {
		return designerid;
	}

	public void setDesignerid(String designerid) {
		this.designerid = designerid;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getRequirementid() {
		return requirementid;
	}

	public void setRequirementid(String requirementid) {
		this.requirementid = requirementid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Requirement getRequirement() {
		return requirement;
	}

	public void setRequirement(Requirement requirement) {
		this.requirement = requirement;
	}

}

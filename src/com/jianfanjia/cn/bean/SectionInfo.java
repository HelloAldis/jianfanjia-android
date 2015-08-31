package com.jianfanjia.cn.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * @class SectionInfo.class
 * @author zhanghao
 * @Decription 此类是工序信息实体类
 * @date 2015-8-31 上午11:50
 *
 */
public class SectionInfo implements Serializable{

	private String _id;
	
	private long start_at;
	
	private long end_at;
	
	private String name;
	
	private int status;
	
	private ArrayList<SectionItemInfo> items;
	
	private CheckInfo ys;
	
	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}
	
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getStart_at() {
		return start_at;
	}

	public void setStart_at(long start_at) {
		this.start_at = start_at;
	}

	public long getEnd_at() {
		return end_at;
	}

	public void setEnd_at(long end_at) {
		this.end_at = end_at;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<SectionItemInfo> getItems() {
		return items;
	}

	public void setItems(ArrayList<SectionItemInfo> items) {
		this.items = items;
	}

	public CheckInfo getYs() {
		return ys;
	}

	public void setYs(CheckInfo ys) {
		this.ys = ys;
	}
	
}

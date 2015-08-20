package com.jianfanjia.cn.bean;

import java.util.ArrayList;

/**
 * @version 1.0
 * @Description 此类是大的工序类
 * @author zhanghao
 * @date 2015-8-19 15:17:18
 *
 */
public class ProcedureInfo {
	
	private String name;//工序名称
	
	private String date;//工序时期
	
	private boolean proIsRequestCheck;//工序是否需要验收?
	
	private boolean proIsFinish;//是否验收
	
	private ArrayList<NodeInfo> nodeList = new ArrayList<NodeInfo>();//大工序所包含的节点?

	public boolean isProIsRequestCheck() {
		return proIsRequestCheck;
	}

	public void setProIsRequestCheck(boolean proIsRequestCheck) {
		this.proIsRequestCheck = proIsRequestCheck;
	}

	public boolean isProIsFinish() {
		return proIsFinish;
	}

	public void setProIsFinish(boolean proIsFinish) {
		this.proIsFinish = proIsFinish;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public ArrayList<NodeInfo> getNodeList() {
		return nodeList;
	}

	public void setNodeList(ArrayList<NodeInfo> nodeList) {
		this.nodeList = nodeList;
	}
	
}

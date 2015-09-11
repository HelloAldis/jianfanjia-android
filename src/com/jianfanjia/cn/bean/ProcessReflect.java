package com.jianfanjia.cn.bean;

import java.io.Serializable;

/**
 * @des 当前的工地映射
 * @author zhanghao
 * @date 2015-9-10
 *
 */
public class ProcessReflect implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String processId;
	
	private String ownerId;
	
	private String designerId;
	
	public ProcessReflect(String processId,String ownerId,String designerId){
		this.processId = processId;
		this.ownerId = ownerId;
		this.designerId = designerId;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public String getDesignerId() {
		return designerId;
	}

	public void setDesignerId(String designerId) {
		this.designerId = designerId;
	}
	
}

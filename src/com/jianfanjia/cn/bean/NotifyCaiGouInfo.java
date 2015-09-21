package com.jianfanjia.cn.bean;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @Class NotifyCaiGouInfo
 * @Decription 采购信息实体类
 * @author zhanghao
 * @date 2015-8-26 下午17:31
 * 
 */
@DatabaseTable(tableName = "NotifyCaiGouInfo")
public class NotifyCaiGouInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	@DatabaseField(generatedId = true)
	public int id;// 自增长
	@DatabaseField
	private String title;// 提醒标题
	@DatabaseField
	private String content;// 提醒内容
	@DatabaseField
	private String time;// 提醒时间
	@DatabaseField
	private String stage;// 提醒阶段

	public NotifyCaiGouInfo() {

	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

}

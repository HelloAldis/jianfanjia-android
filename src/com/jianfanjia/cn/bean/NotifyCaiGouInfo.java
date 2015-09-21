package com.jianfanjia.cn.bean;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @Class NotifyCaiGouInfo
 * @Decription �ɹ���Ϣʵ����
 * @author zhanghao
 * @date 2015-8-26 ����17:31
 * 
 */
@DatabaseTable(tableName = "NotifyCaiGouInfo")
public class NotifyCaiGouInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	@DatabaseField(generatedId = true)
	public int id;// ������
	@DatabaseField
	private String title;// ���ѱ���
	@DatabaseField
	private String content;// ��������
	@DatabaseField
	private String time;// ����ʱ��
	@DatabaseField
	private String stage;// ���ѽ׶�

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

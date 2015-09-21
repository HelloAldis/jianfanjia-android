package com.jianfanjia.cn.bean;

import java.io.Serializable;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 
 * @ClassName: NotifyMessage
 * @Description: ������Ϣ
 * @author fengliang
 * @date 2015-9-2 ����9:58:19
 * 
 */
@DatabaseTable(tableName = "NotifyMessage")
public class NotifyMessage implements Serializable {

	private static final long serialVersionUID = -1386644478824610283L;

	@DatabaseField(generatedId = true)
	public int id;// ������

	@DatabaseField
	private String type;// ����

	@DatabaseField
	private String content;// ����

	@DatabaseField
	private String time;// ����

	@DatabaseField
	private String stage;// �׶�

	public NotifyMessage() {

	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

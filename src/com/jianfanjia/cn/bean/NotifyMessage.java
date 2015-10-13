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
	private long time;// ����

	@DatabaseField
	private String section;// �׶�

	@DatabaseField
	private String status;// ״̬

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

}

package com.jianfanjia.cn.bean;

import java.io.Serializable;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 
 * @ClassName: NotifyMessage
 * @Description: 推送消息
 * @author fengliang
 * @date 2015-9-2 上午9:58:19
 * 
 */
@DatabaseTable(tableName = "NotifyMessage")
public class NotifyMessage implements Serializable {

	private static final long serialVersionUID = -1386644478824610283L;

	@DatabaseField(generatedId = true)
	public int id;// 自增长

	@DatabaseField
	private String type;// 类型

	@DatabaseField
	private String content;// 内容

	@DatabaseField
	private String time;// 日期

	@DatabaseField
	private String stage;// 阶段

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

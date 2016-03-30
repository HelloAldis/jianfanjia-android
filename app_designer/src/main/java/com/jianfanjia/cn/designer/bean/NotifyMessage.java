package com.jianfanjia.cn.designer.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * @author fengliang
 * @ClassName: NotifyMessage
 * @Description: 推送消息
 * @date 2015-9-2 上午9:58:19
 */
@DatabaseTable(tableName = "NotifyMessage")
public class NotifyMessage implements Serializable {

    @DatabaseField(generatedId = true)
    public int id;// 自增长

    @DatabaseField(columnName = "type")
    private String type;// 类型

    @DatabaseField(columnName = "cell")
    private String cell;//小区

    @DatabaseField(columnName = "content")
    private String content;// 内容

    @DatabaseField(columnName = "time")
    private long time;// 日期

    @DatabaseField(columnName = "section")
    private String section;// 阶段

    @DatabaseField(columnName = "status")
    private String status;// 状态

    @DatabaseField(columnName = "processid")
    private String processid;// 工序id

    @DatabaseField(columnName = "userid")
    private String userid;

    private String messageid;

    private String badge;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCell() {
        return cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

    public String getProcessid() {
        return processid;
    }

    public void setProcessid(String processid) {
        this.processid = processid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getMessageid() {
        return messageid;
    }

    public void setMessageid(String messageid) {
        this.messageid = messageid;
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }
}

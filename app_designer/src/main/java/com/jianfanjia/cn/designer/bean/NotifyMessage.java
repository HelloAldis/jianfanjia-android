package com.jianfanjia.cn.designer.bean;

import java.io.Serializable;

/**
 * @author fengliang
 * @ClassName: NotifyMessage
 * @Description: 推送消息
 * @date 2015-9-2 上午9:58:19
 */
public class NotifyMessage implements Serializable {

    public int id;// 自增长

    private String type;// 类型

    private String cell;//小区

    private String content;// 内容

    private long time;// 日期

    private String section;// 阶段

    private String status;// 状态

    private String processid;// 工序id

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

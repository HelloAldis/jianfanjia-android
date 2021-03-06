package com.jianfanjia.api.model;

/**
 * Created by Aldis on 16/3/28.
 */
public class Comment extends BaseModel {
    private String _id;
    private String topicid;
    private String topictype;
    private String content;
    private String to;
    private String to_commentid;
    private String by;
    private String usertype;
    private long date;
    private String status;
    private User byUser;
    private SuperVisor bySupervisor;
    private Designer byDesigner;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTopicid() {
        return topicid;
    }

    public void setTopicid(String topicid) {
        this.topicid = topicid;
    }

    public String getTopictype() {
        return topictype;
    }

    public void setTopictype(String topictype) {
        this.topictype = topictype;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getBy() {
        return by;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getByUser() {
        return byUser;
    }

    public SuperVisor getBySupervisor() {
        return bySupervisor;
    }

    public void setBySupervisor(SuperVisor bySupervisor) {
        this.bySupervisor = bySupervisor;
    }

    public Designer getByDesigner() {
        return byDesigner;
    }

    public void setByDesigner(Designer byDesigner) {
        this.byDesigner = byDesigner;
    }

    public void setByUser(User byUser) {
        this.byUser = byUser;
    }

    public String getTo_commentid() {
        return to_commentid;
    }

    public void setTo_commentid(String to_commentid) {
        this.to_commentid = to_commentid;
    }

}

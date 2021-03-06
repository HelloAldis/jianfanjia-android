package com.jianfanjia.api.model;

/**
 * Created by Aldis on 16/3/28.
 */
public class UserMessage extends BaseModel {

    private String _id;
    private String userid;
    private String designerid;
    private String requirementid;
    private String planid;
    private String processid;
    private String topicid;
    private String commentid;
    private String section;
    private String item;
    private String title;
    private String content;
    private String message_type;
    private long create_at;
    private long lastupdate;
    private String status;
    private String html;
    private Requirement requirement;
    private Designer designer;
    private Plan plan;
    private Process process;
    private User user;
    private SuperVisor supervisor;
    private Reschedule reschedule;
    private DiaryInfo diary;
    private Comment to_comment;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getDesignerid() {
        return designerid;
    }

    public void setDesignerid(String designerid) {
        this.designerid = designerid;
    }

    public String getRequirementid() {
        return requirementid;
    }

    public void setRequirementid(String requirementid) {
        this.requirementid = requirementid;
    }

    public String getPlanid() {
        return planid;
    }

    public void setPlanid(String planid) {
        this.planid = planid;
    }

    public String getProcessid() {
        return processid;
    }

    public void setProcessid(String processid) {
        this.processid = processid;
    }

    public String getTopicid() {
        return topicid;
    }

    public void setTopicid(String topicid) {
        this.topicid = topicid;
    }

    public String getCommentid() {
        return commentid;
    }

    public void setCommentid(String commentid) {
        this.commentid = commentid;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
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

    public String getMessage_type() {
        return message_type;
    }

    public void setMessage_type(String message_type) {
        this.message_type = message_type;
    }

    public long getCreate_at() {
        return create_at;
    }

    public void setCreate_at(long create_at) {
        this.create_at = create_at;
    }

    public long getLastupdate() {
        return lastupdate;
    }

    public void setLastupdate(long lastupdate) {
        this.lastupdate = lastupdate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Requirement getRequirement() {
        return requirement;
    }

    public void setRequirement(Requirement requirement) {
        this.requirement = requirement;
    }

    public Designer getDesigner() {
        return designer;
    }

    public void setDesigner(Designer designer) {
        this.designer = designer;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public Reschedule getReschedule() {
        return reschedule;
    }

    public void setReschedule(Reschedule reschedule) {
        this.reschedule = reschedule;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public SuperVisor getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(SuperVisor supervisor) {
        this.supervisor = supervisor;
    }

    public DiaryInfo getDiary() {
        return diary;
    }

    public void setDiary(DiaryInfo diary) {
        this.diary = diary;
    }

    public Comment getTo_comment() {
        return to_comment;
    }

    public void setTo_comment(Comment to_comment) {
        this.to_comment = to_comment;
    }
}

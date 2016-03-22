package com.jianfanjia.cn.bean;

import java.io.Serializable;

/**
 * Name: NoticeDetailInfo
 * User: fengliang
 * Date: 2016-03-10
 * Time: 14:23
 */
public class NoticeDetailInfo implements Serializable {
    private String _id;
    private long create_at;
    private String userid;
    private String designerid;
    private String planid;
    private String requirementid;
    private String processid;
    private String rescheduleid;
    private String section;
    private String title;
    private String content;
    private String html;
    private String message_type;
    private String status;
    private int __v;
    private long lastupdate;
    private ProcessInfo process;
    private RequirementInfo requirement;
    private RescheduleInfo reschedule;
    private PlanInfo plan;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public long getCreate_at() {
        return create_at;
    }

    public void setCreate_at(long create_at) {
        this.create_at = create_at;
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

    public String getPlanid() {
        return planid;
    }

    public void setPlanid(String planid) {
        this.planid = planid;
    }

    public String getRequirementid() {
        return requirementid;
    }

    public void setRequirementid(String requirementid) {
        this.requirementid = requirementid;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int get__v() {
        return __v;
    }

    public void set__v(int __v) {
        this.__v = __v;
    }

    public long getLastupdate() {
        return lastupdate;
    }

    public void setLastupdate(long lastupdate) {
        this.lastupdate = lastupdate;
    }

    public String getProcessid() {
        return processid;
    }

    public void setProcessid(String processid) {
        this.processid = processid;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public ProcessInfo getProcess() {
        return process;
    }

    public void setProcess(ProcessInfo process) {
        this.process = process;
    }

    public RequirementInfo getRequirement() {
        return requirement;
    }

    public void setRequirement(RequirementInfo requirement) {
        this.requirement = requirement;
    }

    public String getRescheduleid() {
        return rescheduleid;
    }

    public void setRescheduleid(String rescheduleid) {
        this.rescheduleid = rescheduleid;
    }

    public RescheduleInfo getReschedule() {
        return reschedule;
    }

    public void setReschedule(RescheduleInfo reschedule) {
        this.reschedule = reschedule;
    }

    public PlanInfo getPlan() {
        return plan;
    }

    public void setPlan(PlanInfo plan) {
        this.plan = plan;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }
}

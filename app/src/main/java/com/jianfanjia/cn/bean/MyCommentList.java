package com.jianfanjia.cn.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Description: com.jianfanjia.cn.bean
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-03-09 14:41
 */
public class MyCommentList implements Serializable {

    private static final long serialVersionUID = -4217175500325441089L;

    private List<MyCommentInfo> comments;
    private int total;

    public List<MyCommentInfo> getComments() {
        return comments;
    }

    public void setComments(List<MyCommentInfo> comments) {
        this.comments = comments;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public static class MyCommentInfo implements Serializable {

        private static final long serialVersionUID = 2913385405256218141L;

        private String _id;
        private String topicid;
        private String topictype;
        private String content;
        private String usertype;
        private String to;
        private String section;
        private String by;
        private String item;
        private long date;
        private String status;
        private int __v;
        private User byUser;

        private PlandetailInfo planInfo;
        private ProcessInfo process;

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String getItem() {
            return item;
        }

        public void setItem(String item) {
            this.item = item;
        }

        public String getSection() {
            return section;
        }

        public void setSection(String section) {
            this.section = section;
        }

        public String getBy() {
            return by;
        }

        public void setBy(String by) {
            this.by = by;
        }

        public PlandetailInfo getPlanInfo() {
            return planInfo;
        }

        public void setPlanInfo(PlandetailInfo planInfo) {
            this.planInfo = planInfo;
        }

        public ProcessInfo getProcess() {
            return process;
        }

        public void setProcess(ProcessInfo process) {
            this.process = process;
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

        public int get__v() {
            return __v;
        }

        public void set__v(int __v) {
            this.__v = __v;
        }

        public User getByUser() {
            return byUser;
        }

        public void setByUser(User byUser) {
            this.byUser = byUser;
        }

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
    }
}

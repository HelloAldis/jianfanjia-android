package com.jianfanjia.api.request.common;

import com.jianfanjia.api.request.BaseRequest;

/**
 * Name: AddCommentRequest
 * User: fengliang
 * Date: 2015-10-23
 * Time: 19:33
 */
public class AddCommentRequest extends BaseRequest {
    private String topicid;
    private String topictype;
    private String content;
    private String to_designerid;
    private String to_userid;
    private String section;
    private String item;
    private String to_commentid;

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

    public String getTo_designerid() {
        return to_designerid;
    }

    public void setTo_designerid(String to_designerid) {
        this.to_designerid = to_designerid;
    }

    public String getTo_userid() {
        return to_userid;
    }

    public void setTo_userid(String to_userid) {
        this.to_userid = to_userid;
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

    public String getTo_commentid() {
        return to_commentid;
    }

    public void setTo_commentid(String to_commentid) {
        this.to_commentid = to_commentid;
    }
}

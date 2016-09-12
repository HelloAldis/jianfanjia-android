package com.jianfanjia.api.request.common;


import com.jianfanjia.api.request.BaseRequest;

/**
 * Name: GetCommentsRequest
 * User: fengliang
 * Date: 2015-10-23
 * Time: 19:42
 */
public class GetCommentsRequest extends BaseRequest {
    private String topicid;
    private String section;
    private String item;
    private int from;
    private int limit;

    public String getTopicid() {
        return topicid;
    }

    public void setTopicid(String topicid) {
        this.topicid = topicid;
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

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}

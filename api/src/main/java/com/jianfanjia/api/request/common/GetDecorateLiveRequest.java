package com.jianfanjia.api.request.common;

import com.jianfanjia.api.request.BaseRequest;

import java.util.Map;

/**
 * Name: GetDecorateLiveRequest
 * User: fengliang
 * Date: 2016-03-28
 * Time: 17:27
 */
public class GetDecorateLiveRequest extends BaseRequest {

    private Map<String, Object> query;

    private int from;

    private int limit;

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

    public Map<String, Object> getQuery() {
        return query;
    }

    public void setQuery(Map<String, Object> query) {
        this.query = query;
    }
}

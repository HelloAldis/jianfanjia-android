package com.jianfanjia.api.request.guest;

import com.jianfanjia.api.request.BaseRequest;

import java.util.Map;

/**
 * Name: SearchDesignerProductRequest
 * User: fengliang
 * Date: 2015-10-20
 * Time: 16:35
 */
public class SearchDesignerProductRequest extends BaseRequest {
    private Map<String, Object> query;
    private int from;
    private int limit;

    public Map<String, Object> getQuery() {
        return query;
    }

    public void setQuery(Map<String, Object> query) {
        this.query = query;
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

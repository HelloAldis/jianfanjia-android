package com.jianfanjia.api.request.guest;

import com.jianfanjia.api.request.BaseRequest;

import java.util.Map;

/**
 * Created by Aldis on 16/3/28.
 */
public class SearchDecorationImgRequest extends BaseRequest {
    private Map<String, Object> query;
    private String search_word;
    private Map<String, Object> sort;
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

    public String getSearch_word() {
        return search_word;
    }

    public void setSearch_word(String search_word) {
        this.search_word = search_word;
    }

    public Map<String, Object> getSort() {
        return sort;
    }

    public void setSort(Map<String, Object> sort) {
        this.sort = sort;
    }
}

package com.jianfanjia.api.request.guest;

import com.jianfanjia.api.request.BaseRequest;

import java.util.Map;

/**
 * Created by Aldis on 16/3/28.
 */
public class SearchDecorationImgRequest extends BaseRequest {
    private Map<String, Object> query;

    public Map<String, Object> getQuery() {
        return query;
    }

    public void setQuery(Map<String, Object> query) {
        this.query = query;
    }
}

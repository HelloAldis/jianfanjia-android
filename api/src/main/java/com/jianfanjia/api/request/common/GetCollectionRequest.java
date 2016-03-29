package com.jianfanjia.api.request.common;

import com.jianfanjia.api.request.BaseRequest;

/**
 * Name: GetCollectionRequest
 * User: fengliang
 * Date: 2016-03-28
 * Time: 17:01
 */
public class GetCollectionRequest extends BaseRequest {
    private int from;
    private int limit;

    public GetCollectionRequest(int from, int limit) {
        this.from = from;
        this.limit = limit;
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
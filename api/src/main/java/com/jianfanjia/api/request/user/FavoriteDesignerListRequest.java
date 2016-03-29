package com.jianfanjia.api.request.user;

import com.jianfanjia.api.request.BaseRequest;

/**
 * Name: FavoriteDesignerListRequest
 * User: fengliang
 * Date: 2016-03-28
 * Time: 16:49
 */
public class FavoriteDesignerListRequest extends BaseRequest {
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
}

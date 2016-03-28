package com.jianfanjia.api.request.guest;

import com.jianfanjia.api.request.BaseRequest;

/**
 * Name: GetHomeProductRequest
 * User: fengliang
 * Date: 2016-03-28
 * Time: 18:23
 */
public class GetHomeProductRequest extends BaseRequest {
    private int limit;

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}

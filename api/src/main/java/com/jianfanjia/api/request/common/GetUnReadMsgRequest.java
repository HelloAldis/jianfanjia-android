package com.jianfanjia.api.request.common;

import com.jianfanjia.api.request.BaseRequest;

import java.util.List;

/**
 * Name: GetUnReadMsgRequest
 * User: fengliang
 * Date: 2016-03-29
 * Time: 11:33
 */
public class GetUnReadMsgRequest extends BaseRequest {

    private List<String[]> query_array;

    public List<String[]> getQuery_array() {
        return query_array;
    }

    public void setQuery_array(List<String[]> query_array) {
        this.query_array = query_array;
    }
}

package com.jianfanjia.api.request.common;

import com.jianfanjia.api.request.BaseRequest;

/**
 * Name: AddProductFavoriteRequest
 * User: fengliang
 * Date: 2015-12-08
 * Time: 15:54
 */
public class AddProductFavoriteRequest extends BaseRequest {
    private String _id;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}

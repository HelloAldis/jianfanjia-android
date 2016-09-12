package com.jianfanjia.api.request.common;

import com.jianfanjia.api.request.BaseRequest;

/**
 * Name: DeleteBeautyImgRequest
 * User: fengliang
 * Date: 2015-12-11
 * Time: 14:09
 */
public class DeleteBeautyImgRequest extends BaseRequest {
    private String _id;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}

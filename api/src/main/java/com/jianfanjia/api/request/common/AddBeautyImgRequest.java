package com.jianfanjia.api.request.common;

import com.jianfanjia.api.request.BaseRequest;

/**
 * Created by Aldis on 16/3/28.
 */
public class AddBeautyImgRequest extends BaseRequest {
    private String _id;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}

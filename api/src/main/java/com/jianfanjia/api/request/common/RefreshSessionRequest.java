package com.jianfanjia.api.request.common;

import com.jianfanjia.api.request.BaseRequest;

/**
 * Description: com.jianfanjia.api.request.common
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-03-28 17:09
 */
public class RefreshSessionRequest extends BaseRequest{

    private String _id;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}

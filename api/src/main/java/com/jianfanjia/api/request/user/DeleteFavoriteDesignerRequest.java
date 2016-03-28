package com.jianfanjia.api.request.user;


import com.jianfanjia.api.request.BaseRequest;

/**
 * Description:com.jianfanjia.cn.http.request
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-20 21:28
 */
public class DeleteFavoriteDesignerRequest extends BaseRequest {
    private String _id;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}

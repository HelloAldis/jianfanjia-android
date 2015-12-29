package com.jianfanjia.cn.http.request;

import android.content.Context;

import com.jianfanjia.cn.base.BaseRequest;

/**
 * Name: DeleteBeautyImgRequest
 * User: fengliang
 * Date: 2015-12-11
 * Time: 14:09
 */
public class DeleteBeautyImgRequest extends BaseRequest {
    private String _id;

    public DeleteBeautyImgRequest(Context context, String _id) {
        super(context);
        this._id = _id;
        url = url_new.DELETE_BEAUTY_IMG_BY_USER;
    }

    @Override
    public void pre() {
        super.pre();
    }

    @Override
    public void all() {
        super.all();
    }

    @Override
    public void onSuccess(Object data) {
        super.onSuccess(data);
    }
}

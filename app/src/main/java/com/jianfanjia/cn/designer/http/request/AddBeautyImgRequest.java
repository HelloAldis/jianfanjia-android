package com.jianfanjia.cn.designer.http.request;

import android.content.Context;

import com.jianfanjia.cn.designer.base.BaseRequest;

/**
 * Name: AddBeautyImgRequest
 * User: fengliang
 * Date: 2015-12-16
 * Time: 16:09
 */
public class AddBeautyImgRequest extends BaseRequest {
    private String id;

    public AddBeautyImgRequest(Context context, String id) {
        super(context);
        this.id = id;
        url = url_new.ADD_BEAUTY_IMG;
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

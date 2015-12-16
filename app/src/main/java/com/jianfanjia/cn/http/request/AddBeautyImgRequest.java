package com.jianfanjia.cn.http.request;

import android.content.Context;

import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.config.Url_New;

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
        url = Url_New.ADD_BEAUTY_IMG;
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

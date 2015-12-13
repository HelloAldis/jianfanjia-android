package com.jianfanjia.cn.http.request;

import android.content.Context;

import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.config.Url_New;

/**
 * Name: GetBeautyImgListRequest
 * User: fengliang
 * Date: 2015-12-11
 * Time: 14:07
 */
public class GetBeautyImgListRequest extends BaseRequest {
    private int from;
    private int limit;

    public GetBeautyImgListRequest(Context context, int from, int limit) {
        super(context);
        this.from = from;
        this.limit = limit;
        url = Url_New.GET_BEAUTY_IMG_LIST_BY_USER;
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

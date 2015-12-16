package com.jianfanjia.cn.http.request;

import android.content.Context;

import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.config.Url_New;

/**
 * Name: GetDecorationImgRequest
 * User: fengliang
 * Date: 2015-12-08
 * Time: 17:02
 */
public class GetDecorationImgRequest extends BaseRequest {
    private String _id;

    public GetDecorationImgRequest(Context context, String _id) {
        super(context);
        this._id = _id;
        url = Url_New.GET_DECORATION_IMG;
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

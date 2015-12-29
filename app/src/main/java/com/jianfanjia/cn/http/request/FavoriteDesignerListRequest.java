package com.jianfanjia.cn.http.request;

import android.content.Context;

import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.config.Url_New;

public class FavoriteDesignerListRequest extends BaseRequest {
    private int from;
    private int limit;

    public FavoriteDesignerListRequest(Context context, int from, int limit) {
        super(context);
        this.from = from;
        this.limit = limit;
        url = url_new.FAVORITE_DESIGNER_LIST;
    }

    @Override
    public void pre() {
        super.pre();
    }

    @Override
    public void onSuccess(Object data) {
        super.onSuccess(data);
        if (data != null) {

        }
    }


}

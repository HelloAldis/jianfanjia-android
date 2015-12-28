package com.jianfanjia.cn.http.request;

import android.content.Context;

import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.config.Url_New;

/**
 * Name: HomePageRequest
 * User: fengliang
 * Date: 2015-10-20
 * Time: 10:49
 */
public class HomePageRequest extends BaseRequest {
    private int from;
    private int limit;

    public HomePageRequest(Context context, int from, int limit) {
        super(context);
        this.from = from;
        this.limit = limit;
        url = url_new.HOME_PAGE_DISIGNERS;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    @Override
    public void onSuccess(Object data) {
        super.onSuccess(data);
    }

    @Override
    public void all() {
        super.all();
    }

    @Override
    public void pre() {
        super.pre();
    }
}

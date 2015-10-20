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
    private String from;
    private String limit;

    public HomePageRequest(Context context, String from, String limit) {
        super(context);
        this.from = from;
        this.limit = limit;
        url = Url_New.HOME_PAGE_DISIGNERS;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
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

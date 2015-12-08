package com.jianfanjia.cn.http.request;

import android.content.Context;

import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.config.Url_New;

/**
 * Name: SearchDecorationImgRequest
 * User: fengliang
 * Date: 2015-12-08
 * Time: 17:01
 */
public class SearchDecorationImgRequest extends BaseRequest {
    private String searchWord;
    private int from;
    private int limit;

    public SearchDecorationImgRequest(Context context, String searchWord, int from, int limit) {
        super(context);
        this.searchWord = searchWord;
        this.from = from;
        this.limit = limit;
        url = Url_New.SEARCH_DECORATION_IMG;
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
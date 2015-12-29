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
    private String section;
    private String house_type;
    private String dec_style;
    private String searchWord;
    private int lastupdate;
    private int from;
    private int limit;

    public SearchDecorationImgRequest(Context context, String section, String house_type, String dec_style, String searchWord, int lastupdate, int from, int limit) {
        super(context);
        this.section = section;
        this.house_type = house_type;
        this.dec_style = dec_style;
        this.searchWord = searchWord;
        this.lastupdate = lastupdate;
        this.from = from;
        this.limit = limit;
        url = url_new.SEARCH_DECORATION_IMG;
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

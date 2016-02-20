package com.jianfanjia.cn.http.request;

import android.content.Context;

import com.jianfanjia.cn.base.BaseRequest;

/**
 * Name: SearchDesignerRequest
 * User: fengliang
 * Date: 2016-02-20
 * Time: 09:52
 */
public class SearchDesignerRequest extends BaseRequest {
    private String decType;
    private String decHouseType;
    private String decStyle;
    private String decFee;
    private String searchWord;
    private int authedProductCount;
    private int from;
    private int limit;

    public SearchDesignerRequest(Context context, String decType, String decHouseType, String decStyle, String decFee, String searchWord, int authedProductCount, int from, int limit) {
        super(context);
        this.decType = decType;
        this.decHouseType = decHouseType;
        this.decStyle = decStyle;
        this.decFee = decFee;
        this.searchWord = searchWord;
        this.authedProductCount = authedProductCount;
        this.from = from;
        this.limit = limit;
        url = url_new.SEARCH_DESIGNER;
    }
}

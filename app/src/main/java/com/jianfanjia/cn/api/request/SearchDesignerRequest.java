package com.jianfanjia.cn.api.request;

import android.content.Context;

import java.util.Map;

/**
 * Created by jyz on 16/2/24.
 */
public class SearchDesignerRequest extends BaseRequest {
    public Map<String,Object> query;
    public String search_word;
    public Map<String,Object> sort;
    public int from;
    public int limit;
}

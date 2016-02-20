package com.jianfanjia.cn.http.request;

import android.content.Context;

import com.jianfanjia.cn.base.BaseRequest;

/**
 * Description:com.jianfanjia.cn.http.request
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-20 21:57
 */
public class GetHomeProductRequest extends BaseRequest {

    public GetHomeProductRequest(Context context) {
        super(context);
        url = url_new.GET_TOP_PRODUCTS;
    }

}

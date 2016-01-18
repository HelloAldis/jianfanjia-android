package com.jianfanjia.cn.designer.http.request;

import android.content.Context;

import com.jianfanjia.cn.designer.base.BaseRequest;

/**
 * Description:com.jianfanjia.cn.http.request
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-20 21:57
 */
public class GetProductHomePageRequest extends BaseRequest {
    private String productid;

    public GetProductHomePageRequest(Context context, String productid) {
        super(context);
        this.productid = productid;
        url = url_new.PRODUCT_HOME_PAGE;
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

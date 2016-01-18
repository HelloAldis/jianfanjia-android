package com.jianfanjia.cn.designer.http.request;

import android.content.Context;

import com.jianfanjia.cn.designer.base.BaseRequest;

/**
 * Description:com.jianfanjia.cn.http.request
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-20 21:28
 */
public class AddFavoriteDesignerRequest extends BaseRequest {
    private String designerid;


    public AddFavoriteDesignerRequest(Context context, String designerid) {
        super(context);
        this.designerid = designerid;
        url = url_new.ADD_FAVORITE_DESIGNER;
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

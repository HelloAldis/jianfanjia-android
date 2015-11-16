package com.jianfanjia.cn.http.request;

import android.content.Context;

import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.config.Url_New;

/**
 * Description:com.jianfanjia.cn.http.request
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-20 21:28
 */
public class DeleteFavoriteDesignerRequest extends BaseRequest {
    private String designerid;


    public DeleteFavoriteDesignerRequest(Context context, String designerid) {
        super(context);
        this.designerid = designerid;
        url = Url_New.DELETE_FAVORITE_DESIGNER;
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

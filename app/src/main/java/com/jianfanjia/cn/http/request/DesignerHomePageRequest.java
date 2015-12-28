package com.jianfanjia.cn.http.request;

import android.content.Context;

import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.config.Url_New;

/**
 * Name: DesignerHomePageRequest
 * User: fengliang
 * Date: 2015-10-20
 * Time: 16:27
 */
public class DesignerHomePageRequest extends BaseRequest {
    private String designerid;

    public DesignerHomePageRequest(Context context, String designerid) {
        super(context);
        this.designerid = designerid;
        url = url_new.DESIGNER_HOME_PAGE;
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

package com.jianfanjia.cn.designer.http.request;

import android.content.Context;

import com.jianfanjia.cn.designer.application.MyApplication;
import com.jianfanjia.cn.designer.base.BaseRequest;
import com.jianfanjia.cn.designer.config.Url_New;

public class CheckVersionRequest extends BaseRequest {

    public CheckVersionRequest(Context context) {
        super(context);
        url = Url_New.UPDATE_VERSION_URL;
    }

    @Override
    public void all() {
        // TODO Auto-generated method stub
        super.all();

    }

    @Override
    public void pre() {
        // TODO Auto-generated method stub
        super.pre();
    }

    @Override
    public void onSuccess(Object data) {
        if (data.toString() != null) {
            dataManager.setLogin(false);
            dataManager.cleanData();
            MyApplication.getInstance().clearCookie();
        }
    }

}

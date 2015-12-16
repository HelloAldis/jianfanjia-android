package com.jianfanjia.cn.designer.http.request;

import android.content.Context;
import android.text.TextUtils;

import com.jianfanjia.cn.designer.base.BaseRequest;
import com.jianfanjia.cn.designer.config.Url_New;

public class UploadPicRequestNew extends BaseRequest {


    public UploadPicRequestNew(Context context) {
        super(context);
        url = Url_New.UPLOAD_IMAGE;
    }

    @Override
    public void all() {
        super.all();
    }

    @Override
    public void pre() {
        super.pre();
        dataManager.setCurrentUploadImageId(null);
    }

    @Override
    public void onSuccess(Object data) {
        if (!TextUtils.isEmpty(data.toString())) {
            dataManager.setCurrentUploadImageId(data.toString());
        }
    }


}

package com.jianfanjia.cn.designer.http.request;

import android.content.Context;

import com.jianfanjia.cn.designer.base.BaseRequest;
import com.jianfanjia.cn.designer.bean.DesignerInfo;
import com.jianfanjia.cn.designer.tools.JsonParser;

public class DesignerInfoRequest extends BaseRequest {

    public DesignerInfoRequest(Context context) {
        super(context);
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
            DesignerInfo designerInfo = JsonParser
                    .jsonToBean(data.toString(), DesignerInfo.class);
            if (designerInfo != null) {
                dataManager.setDesignerInfo(designerInfo);
            }
        }
    }


}

package com.jianfanjia.cn.designer.http.request;

import android.content.Context;

import com.jianfanjia.cn.designer.base.BaseRequest;
import com.jianfanjia.api.model.Designer;
import com.jianfanjia.cn.designer.bean.RegisterInfo;
import com.jianfanjia.cn.designer.tools.JsonParser;

import java.util.Calendar;

public class RegisterRequest extends BaseRequest {

    private RegisterInfo registerInfo;

    public RegisterRequest(Context context, RegisterInfo registerInfo) {
        super(context);
        this.registerInfo = registerInfo;
        url = url_new.REGISTER_URL;
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
        if (data != null) {
            dataManager.setLogin(true);
            dataManager.savaLastLoginTime(Calendar.getInstance()
                    .getTimeInMillis());
            Designer designer = JsonParser.jsonToBean(data.toString(),Designer.class);
            designer.setPass(registerInfo.getPass());
            dataManager.saveLoginUserBean(designer);
        }
    }

}

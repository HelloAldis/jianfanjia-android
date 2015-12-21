package com.jianfanjia.cn.designer.http.request;

import android.content.Context;

import com.jianfanjia.cn.designer.base.BaseRequest;
import com.jianfanjia.cn.designer.bean.LoginUserBean;
import com.jianfanjia.cn.designer.config.Url_New;
import com.jianfanjia.cn.designer.tools.JsonParser;

import java.util.Calendar;

public class RegisterRequest extends BaseRequest {

    private String password;

    public RegisterRequest(Context context,String password) {
        super(context);
        this.password = password;
        url = Url_New.REGISTER_URL;
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
            LoginUserBean loginUserBean = JsonParser.jsonToBean(data.toString(), LoginUserBean.class);
            loginUserBean.setPass(password);
            dataManager.saveLoginUserInfo(loginUserBean);
            dataManager.setLogin(true);
            dataManager.savaLastLoginTime(Calendar.getInstance()
                    .getTimeInMillis());
        }
    }

}

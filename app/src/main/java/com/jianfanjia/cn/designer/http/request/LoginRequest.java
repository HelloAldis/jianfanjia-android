package com.jianfanjia.cn.designer.http.request;

import android.content.Context;

import com.jianfanjia.cn.designer.base.BaseRequest;
import com.jianfanjia.cn.designer.bean.LoginUserBean;
import com.jianfanjia.cn.designer.tools.JsonParser;
import com.jianfanjia.cn.designer.tools.LogTool;

import java.util.Calendar;

public class LoginRequest extends BaseRequest {
    private String username;
    private String password;

    public LoginRequest(Context context, String username, String password) {
        super(context);
        this.username = username;
        this.password = password;
        url = url_new.LOGIN_URL;
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
        super.onSuccess(data);
        if (data != null) {
            LogTool.d(this.getClass().getName(), "already login");
            dataManager.setLogin(true);
            dataManager.savaLastLoginTime(Calendar.getInstance()
                    .getTimeInMillis());
            LoginUserBean loginUserBean = JsonParser.jsonToBean(data.toString(),LoginUserBean.class);
            loginUserBean.setPass(password);
            dataManager.saveLoginUserBean(loginUserBean);
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}

package com.jianfanjia.cn.http.request;

import android.content.Context;

import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.bean.LoginUserBean;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;

import java.util.Calendar;

/**
 * Description: com.jianfanjia.cn.http.request
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-01-04 18:18
 */
public class WeiXinLoginRequest extends BaseRequest{

    public WeiXinLoginRequest(Context context) {
        super(context);
        url = url_new.WEIXIN_LOGIN_URL;
    }

    @Override
    public void onSuccess(Object data) {
        super.onSuccess(data);
        if (data != null) {
            LogTool.d(this.getClass().getName(), "already login");
            LoginUserBean loginUserBean = JsonParser.jsonToBean((String) data, LoginUserBean.class);
            dataManager.saveLoginUserInfo(loginUserBean);
            dataManager.setLogin(true);
            dataManager.savaLastLoginTime(Calendar.getInstance()
                    .getTimeInMillis());
        }
    }
}

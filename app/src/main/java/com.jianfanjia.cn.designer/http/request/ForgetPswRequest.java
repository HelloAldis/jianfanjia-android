package com.jianfanjia.cn.designer.http.request;

import android.content.Context;

import com.jianfanjia.cn.designer.base.BaseRequest;
import com.jianfanjia.cn.designer.config.Url_New;

/**
 * Description: com.jianfanjia.com.jianfanjia.com.jianfanjia.com.jianfanjia.cn.designer.designer.com.jianfanjia.com.jianfanjia.cn.designer.designer.com.jianfanjia.com.jianfanjia.cn.designer.designer.http.request
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-10-27 17:40
 */
public class ForgetPswRequest extends BaseRequest {

    public ForgetPswRequest(Context context) {
        super(context);
        url = Url_New.UPDATE_PASS_URL;
    }

    @Override
    public void onSuccess(Object data) {
        super.onSuccess(data);
       /* if (data != null) {
            LoginUserBean loginUserBean = JsonParser.jsonToBean((String) data, LoginUserBean.class);
            loginUserBean.setPass(password);
            dataManager.saveLoginUserInfo(loginUserBean);
            dataManager.setLogin(true);
            dataManager.savaLastLoginTime(Calendar.getInstance()
                    .getTimeInMillis());
        }*/
    }


}

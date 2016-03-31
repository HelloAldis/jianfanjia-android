package com.jianfanjia.cn.designer.http.request;

import android.content.Context;

import com.jianfanjia.cn.designer.base.BaseRequest;
import com.jianfanjia.api.model.Designer;
import com.jianfanjia.cn.designer.bean.WeiXinRegisterInfo;
import com.jianfanjia.cn.designer.tools.JsonParser;
import com.jianfanjia.cn.designer.tools.LogTool;

import java.util.Calendar;

/**
 * Description: com.jianfanjia.cn.http.request
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-01-04 18:18
 */
public class WeiXinLoginRequest extends BaseRequest{

    WeiXinRegisterInfo weiXinRegisterInfo;

    public WeiXinLoginRequest(Context context,WeiXinRegisterInfo weiXinRegisterInfo) {
        super(context);
        url = url_new.WEIXIN_LOGIN_URL;
        this.weiXinRegisterInfo = weiXinRegisterInfo;
    }

    @Override
    public void onSuccess(Object data) {
        super.onSuccess(data);
        if (data != null) {
            LogTool.d(this.getClass().getName(), "already login");
            dataManager.setLogin(true);
            dataManager.savaLastLoginTime(Calendar.getInstance()
                    .getTimeInMillis());
            Designer designer = JsonParser.jsonToBean(data.toString(),Designer.class);
            designer.setWechat_openid(weiXinRegisterInfo.getWechat_openid());
            designer.setWechat_unionid(weiXinRegisterInfo.getWechat_unionid());
            dataManager.saveLoginUserBean(designer);
        }
    }
}

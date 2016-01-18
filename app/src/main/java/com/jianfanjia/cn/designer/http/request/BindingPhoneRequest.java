package com.jianfanjia.cn.designer.http.request;

import android.content.Context;

import com.jianfanjia.cn.designer.base.BaseRequest;
import com.jianfanjia.cn.designer.tools.LogTool;

/**
 * Description: com.jianfanjia.cn.http.request
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-01-05 19:54
 */
public class BindingPhoneRequest extends BaseRequest{

    String account = null;

    public BindingPhoneRequest(Context context,String account) {
        super(context);
        url = url_new.BIND_PHONE;
        this.account = account;
    }

    @Override
    public void onSuccess(Object data) {
        super.onSuccess(data);
        if(data != null){
            LogTool.d(this.getClass().getName(),data.toString());
            dataManager.setAccount(account);
        }
    }
}

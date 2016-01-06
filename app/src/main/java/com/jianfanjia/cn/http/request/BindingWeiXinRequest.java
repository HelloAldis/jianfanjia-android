package com.jianfanjia.cn.http.request;

import android.content.Context;

import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.tools.LogTool;

/**
 * Description: com.jianfanjia.cn.http.request
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-01-05 19:54
 */
public class BindingWeiXinRequest extends BaseRequest{

    private String unionid;

    public BindingWeiXinRequest(Context context,String union_id) {
        super(context);
        url = url_new.BIND_WEIXIN;
        this.unionid = union_id;
    }

    @Override
    public void onSuccess(Object data) {
        super.onSuccess(data);
        if(data != null){
            LogTool.d(this.getClass().getName(),data.toString());
            dataManager.setWeixinFisrtLogin(false);
            dataManager.setWechat_unionid(unionid);
        }
    }
}

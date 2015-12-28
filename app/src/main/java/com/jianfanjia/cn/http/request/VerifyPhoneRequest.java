package com.jianfanjia.cn.http.request;

import android.content.Context;

import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.config.Url_New;

/**
 * Description: com.jianfanjia.cn.http.request
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-11-13 16:21
 */
public class VerifyPhoneRequest extends BaseRequest {

    public VerifyPhoneRequest(Context context){
        super(context);
        url = url_new.VERIFY_PHONE;
    }



}

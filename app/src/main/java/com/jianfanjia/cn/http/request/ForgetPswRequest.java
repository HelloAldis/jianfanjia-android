package com.jianfanjia.cn.http.request;

import android.content.Context;

import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.config.Url_New;

/**
 * Description: com.jianfanjia.cn.http.request
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-10-27 17:40
 */
public class ForgetPswRequest extends BaseRequest{

    public ForgetPswRequest(Context context) {
        super(context);
        url = Url_New.UPDATE_PASS_URL;
    }
}

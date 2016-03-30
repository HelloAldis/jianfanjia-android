package com.jianfanjia.cn.designer.http.request;

import android.content.Context;

import com.jianfanjia.cn.designer.base.BaseRequest;

/**
 * Description: com.jianfanjia.cn.http.request
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-01-05 20:33
 */
public class RefreshSessionRequest extends BaseRequest{

    public RefreshSessionRequest(Context context) {
        super(context);
        url = url_new.REFRESH_SESSION;
    }
}

package com.jianfanjia.cn.designer.http.request;

import android.content.Context;

import com.jianfanjia.cn.designer.base.BaseRequest;

/**
 * Description: com.jianfanjia.cn.http.request
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-12-15 11:42
 */
public class PostCollectOwnerInfoRequest extends BaseRequest {

    public PostCollectOwnerInfoRequest(Context context){
        super(context);
        url = url_new.GET_OWER_INFO;
    }



}

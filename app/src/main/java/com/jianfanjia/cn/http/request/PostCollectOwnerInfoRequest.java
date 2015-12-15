package com.jianfanjia.cn.http.request;

import android.content.Context;

import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.config.Url_New;

/**
 * Description: com.jianfanjia.cn.http.request
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-12-15 11:42
 */
public class PostCollectOwnerInfoRequest extends BaseRequest {

    public PostCollectOwnerInfoRequest(Context context){
        super(context);
        url = Url_New.GET_OWER_INFO;
    }



}

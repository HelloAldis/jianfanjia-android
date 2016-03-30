package com.jianfanjia.cn.designer.http.request;

import android.content.Context;

import com.jianfanjia.cn.designer.base.BaseRequest;

/**
 * Description: com.jianfanjia.cn.http.request
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-11-06 09:35
 */
public class DeletePicToSectionItemRequest extends BaseRequest{

    public DeletePicToSectionItemRequest(Context context) {
        super(context);
        url = url_new.DELETE_PROCESS_PIC;
    }
}

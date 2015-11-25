package com.jianfanjia.cn.http.request;

import android.content.Context;

import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.config.Url_New;

/**
 * Description: com.jianfanjia.cn.http.request
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-11-06 09:35
 */
public class DeletePicToSectionItemRequest extends BaseRequest {

    public DeletePicToSectionItemRequest(Context context) {
        super(context);
        url = Url_New.DELETE_PROCESS_PIC;
    }
}

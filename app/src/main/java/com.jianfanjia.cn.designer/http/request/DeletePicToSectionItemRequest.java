package com.jianfanjia.cn.designer.http.request;

import android.content.Context;

import com.jianfanjia.cn.designer.base.BaseRequest;
import com.jianfanjia.cn.designer.config.Url_New;

/**
 * Description: com.jianfanjia.com.jianfanjia.com.jianfanjia.com.jianfanjia.cn.designer.designer.com.jianfanjia.com.jianfanjia.cn.designer.designer.com.jianfanjia.com.jianfanjia.cn.designer.designer.http.request
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

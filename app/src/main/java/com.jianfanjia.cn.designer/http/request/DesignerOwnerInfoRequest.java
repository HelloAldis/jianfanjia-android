package com.jianfanjia.cn.designer.http.request;

import android.content.Context;

import com.jianfanjia.cn.designer.bean.ProcessInfo;
import com.jianfanjia.cn.designer.tools.JsonParser;

public class DesignerOwnerInfoRequest extends ProcessInfoRequest {

    public DesignerOwnerInfoRequest(Context context, String processId) {
        super(context, processId);
    }

    @Override
    public void onSuccess(Object data) {
        if (data.toString() != null) {
            ProcessInfo processInfo = JsonParser
                    .jsonToBean(data.toString(), ProcessInfo.class);
            if (processInfo != null) {
                dataManager.saveProcessInfo(processInfo);
            }
        }
    }

}

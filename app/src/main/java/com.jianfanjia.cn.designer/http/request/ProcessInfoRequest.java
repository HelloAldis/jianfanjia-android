package com.jianfanjia.cn.designer.http.request;

import android.content.Context;

import com.jianfanjia.cn.designer.base.BaseRequest;
import com.jianfanjia.cn.designer.bean.ProcessInfo;
import com.jianfanjia.cn.designer.tools.JsonParser;

public class ProcessInfoRequest extends BaseRequest {

    private String processId;

    public ProcessInfoRequest(Context context, String processId) {
        super(context);
        this.processId = processId;
    }

    @Override
    public void all() {
        // TODO Auto-generated method stub
        super.all();

    }

    @Override
    public void pre() {
        // TODO Auto-generated method stub
        super.pre();
    }

    @Override
    public void onSuccess(Object data) {
        if (data.toString() != null) {
            ProcessInfo processInfo = JsonParser
                    .jsonToBean(data.toString(), ProcessInfo.class);
            if (processInfo != null) {
                dataManager.setCurrentProcessInfo(processInfo);
                dataManager.saveProcessInfo(processInfo);
            }
        }
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }


}

package com.jianfanjia.cn.http.request;

import android.content.Context;

import com.jianfanjia.cn.base.BaseResponse;
import com.jianfanjia.cn.bean.ProcessInfo;
import com.jianfanjia.cn.tools.JsonParser;

public class DesignerOwnerInfoRequest extends ProcessInfoRequest {
	
	public DesignerOwnerInfoRequest(Context context,String processId) {
		super(context,processId);
	}
	
	@Override
	public void onSuccess(BaseResponse baseResponse) {
		String data = baseResponse.getData().toString();
		if(data != null){
			ProcessInfo processInfo = JsonParser
					.jsonToBean(data, ProcessInfo.class);
			if(processInfo != null){
				dataManager.saveProcessInfo(processInfo);
			}
		}
	}
	
}

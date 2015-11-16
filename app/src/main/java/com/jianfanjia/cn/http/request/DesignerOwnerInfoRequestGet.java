package com.jianfanjia.cn.http.request;

import android.content.Context;

import com.jianfanjia.cn.bean.ProcessInfo;
import com.jianfanjia.cn.tools.JsonParser;

public class DesignerOwnerInfoRequestGet extends GetProcessInfoRequest {
	
	public DesignerOwnerInfoRequestGet(Context context, String processId) {
		super(context,processId);
	}
	
	@Override
	public void onSuccess(Object data) {
		if(data != null){
			ProcessInfo processInfo = JsonParser
					.jsonToBean((String)data, ProcessInfo.class);
			if(processInfo != null){
				dataManager.saveProcessInfo(processInfo);
			}
		}
	}
	
}

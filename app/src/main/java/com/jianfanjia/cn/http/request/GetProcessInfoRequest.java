package com.jianfanjia.cn.http.request;

import android.content.Context;

import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.tools.JsonParser;

public class GetProcessInfoRequest extends BaseRequest {
	
	private String processId;
	
	public GetProcessInfoRequest(Context context, String processId) {
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
		super.onSuccess(data);
		if(data != null){
			Process processInfo = JsonParser
					.jsonToBean((String)data, Process.class);
			if(processInfo != null){
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

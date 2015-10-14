package com.jianfanjia.cn.http.request;

import android.content.Context;

import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.base.BaseResponse;
import com.jianfanjia.cn.bean.ProcessInfo;
import com.jianfanjia.cn.tools.JsonParser;

public class ProcessInfoRequest extends BaseRequest {
	
	private String processId;
	
	public ProcessInfoRequest(Context context,String processId) {
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
	public void onSuccess(BaseResponse baseResponse) {
		super.onSuccess(baseResponse);
		String data = baseResponse.getData().toString();
		if(data != null){
			ProcessInfo processInfo = JsonParser
					.jsonToBean(data, ProcessInfo.class);
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

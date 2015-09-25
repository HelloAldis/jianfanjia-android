package com.jianfanjia.cn.http.request;

import android.content.Context;

import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.base.BaseResponse;
import com.jianfanjia.cn.bean.ProcessInfo;
import com.jianfanjia.cn.bean.RequirementInfo;
import com.jianfanjia.cn.tools.JsonParser;

public class PostRequirementRequest extends BaseRequest {
	
	private RequirementInfo requirementInfo;
	
	public PostRequirementRequest(Context context,RequirementInfo requirementInfo) {
		super(context);
		this.requirementInfo = requirementInfo;
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
		String data = baseResponse.getData().toString();
		if(data != null){
			ProcessInfo processInfo = JsonParser
					.jsonToBean(data, ProcessInfo.class);
			if(processInfo != null){
				dataManager.saveProcessInfo(processInfo);
				dataManager.setCurrentProcessInfo(processInfo);
			}
		}
	}

	public RequirementInfo getRequirementInfo() {
		return requirementInfo;
	}

	public void setRequirementInfo(RequirementInfo requirementInfo) {
		this.requirementInfo = requirementInfo;
	}

}

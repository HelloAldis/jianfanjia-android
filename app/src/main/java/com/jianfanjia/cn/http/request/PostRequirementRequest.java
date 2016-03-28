package com.jianfanjia.cn.http.request;

import android.content.Context;

import com.jianfanjia.cn.base.BaseRequest;

public class PostRequirementRequest extends BaseRequest {
	
	private Requirement requirementInfo;
	
	public PostRequirementRequest(Context context,Requirement requirementInfo) {
		super(context);
		this.requirementInfo = requirementInfo;
		url = url_new.POST_REQUIREMENT;
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
		if(data != null){

		}
	}

	public Requirement getRequirementInfo() {
		return requirementInfo;
	}

	public void setRequirementInfo(Requirement requirementInfo) {
		this.requirementInfo = requirementInfo;
	}

}

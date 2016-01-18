package com.jianfanjia.cn.designer.http.request;

import android.content.Context;

import com.jianfanjia.cn.designer.base.BaseRequest;
import com.jianfanjia.cn.designer.bean.RequirementInfo;

public class PostRequirementRequest extends BaseRequest {
	
	private RequirementInfo requirementInfo;
	
	public PostRequirementRequest(Context context,RequirementInfo requirementInfo) {
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

	public RequirementInfo getRequirementInfo() {
		return requirementInfo;
	}

	public void setRequirementInfo(RequirementInfo requirementInfo) {
		this.requirementInfo = requirementInfo;
	}

}

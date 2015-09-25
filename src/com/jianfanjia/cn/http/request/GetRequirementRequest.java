package com.jianfanjia.cn.http.request;

import android.content.Context;

import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.base.BaseResponse;
import com.jianfanjia.cn.bean.RequirementInfo;
import com.jianfanjia.cn.tools.JsonParser;

public class GetRequirementRequest extends BaseRequest {
	
	public GetRequirementRequest(Context context) {
		super(context);
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
			RequirementInfo requirementInfo = JsonParser.jsonToBean(data,
					RequirementInfo.class);
			if(requirementInfo != null && requirementInfo.get_id() != null){
				requirementInfo.setRequirementid(requirementInfo.get_id());
			}
			dataManager.setRequirementInfo(requirementInfo);
		}
	}
	
	

}

package com.jianfanjia.cn.http.request;

import android.content.Context;

import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.base.BaseResponse;
import com.jianfanjia.cn.bean.OwnerInfo;
import com.jianfanjia.cn.tools.JsonParser;

public class OwnerInfoRequest extends BaseRequest{
	

	private String ownerId;
	
	public OwnerInfoRequest(Context context,String ownerId) {
		super(context);
		this.ownerId = ownerId;
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
			OwnerInfo ownerInfo = JsonParser.jsonToBean(data,
					OwnerInfo.class);
			if(ownerInfo != null){
				dataManager.setOwnerInfo(ownerInfo);
			}
		}
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	


}

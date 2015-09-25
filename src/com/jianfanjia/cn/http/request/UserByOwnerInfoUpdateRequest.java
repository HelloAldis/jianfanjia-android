package com.jianfanjia.cn.http.request;

import android.content.Context;

import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.base.BaseResponse;
import com.jianfanjia.cn.bean.OwnerUpdateInfo;

public class UserByOwnerInfoUpdateRequest extends BaseRequest{
	
	private OwnerUpdateInfo ownerUpdateInfo;
	
	public UserByOwnerInfoUpdateRequest(Context context,OwnerUpdateInfo ownerUpdateInfo) {
		super(context);
		this.ownerUpdateInfo = ownerUpdateInfo;
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
		/*String data = baseResponse.getData().toString();
		if(data != null){
			DesignerInfo designerInfo = JsonParser.jsonToBean(data,
					DesignerInfo.class);
			dataManager.setDesignerInfo(designerInfo);
		}*/
	}

	public OwnerUpdateInfo getOwnerUpdateInfo() {
		return ownerUpdateInfo;
	}

	public void setOwnerUpdateInfo(OwnerUpdateInfo ownerUpdateInfo) {
		this.ownerUpdateInfo = ownerUpdateInfo;
	}

}

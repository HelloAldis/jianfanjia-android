package com.jianfanjia.cn.designer.http.request;

import android.content.Context;

import com.jianfanjia.cn.designer.base.BaseRequest;

public class UpdateOwnerInfoRequest extends BaseRequest{
	
	private OwnerUpdateInfo ownerUpdateInfo;
	
	public UpdateOwnerInfoRequest(Context context, OwnerUpdateInfo ownerUpdateInfo) {
		super(context);
		this.ownerUpdateInfo = ownerUpdateInfo;
		url = url_new.GET_OWER_INFO;
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

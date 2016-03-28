package com.jianfanjia.cn.http.request;

import android.content.Context;

import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.bean.OwnerUpdateInfo;
import com.jianfanjia.cn.config.Url_New;

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
			Designer designerInfo = JsonParser.jsonToBean(data,
					Designer.class);
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

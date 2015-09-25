package com.jianfanjia.cn.http.request;

import android.content.Context;
import android.text.TextUtils;

import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.base.BaseResponse;

public class UploadPicRequest extends BaseRequest {
	
	private String imagePath;
	
	public UploadPicRequest(Context context,String imagePath) {
		super(context);
		this.imagePath = imagePath;
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
		dataManager.setCurrentProcessInfo(null);
	}
	
	@Override
	public void onSuccess(BaseResponse baseResponse) {
		String data = baseResponse.getData().toString();
		if(!TextUtils.isEmpty(data)){
			dataManager.setCurrentUploadImageId(data);
		}
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

}

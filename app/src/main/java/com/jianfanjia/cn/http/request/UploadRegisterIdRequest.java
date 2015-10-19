package com.jianfanjia.cn.http.request;

import android.content.Context;

import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.config.Url;

/**
 * 上传个推id
 */
public class UploadRegisterIdRequest extends BaseRequest {

	private String clientId;

	public UploadRegisterIdRequest(Context context, String clientId) {
		super(context);
		this.clientId = clientId;
		url = Url.BIND_URL;
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
	}
	
	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}


}

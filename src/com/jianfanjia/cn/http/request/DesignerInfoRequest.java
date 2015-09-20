package com.jianfanjia.cn.http.request;

import java.util.Calendar;

import android.content.Context;

import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.base.BaseResponse;
import com.jianfanjia.cn.bean.MyDesignerInfo;
import com.jianfanjia.cn.tools.JsonParser;

public class DesignerInfoRequest extends BaseRequest {
	
	private String designerId;
	
	public DesignerInfoRequest(Context context,String designerId) {
		super(context);
		this.designerId = designerId;
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
			MyDesignerInfo designerInfo = JsonParser
					.jsonToBean(data, MyDesignerInfo.class);
			dataManager.setMyDesignerInfo(designerInfo);
		}
	}

	public String getDesignerId() {
		return designerId;
	}

	public void setDesignerId(String designerId) {
		this.designerId = designerId;
	}

	

	
	
}

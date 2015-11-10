package com.jianfanjia.cn.http.request;

import android.content.Context;

import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.base.BaseResponse;
import com.jianfanjia.cn.bean.DesignerInfo;
import com.jianfanjia.cn.tools.JsonParser;

public class DesignerInfoRequest extends BaseRequest {
	
	public DesignerInfoRequest(Context context) {
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
		super.onSuccess(baseResponse);
		String data = baseResponse.getData().toString();
		if(data != null){
			DesignerInfo designerInfo = JsonParser
					.jsonToBean(data, DesignerInfo.class);
			if(designerInfo != null){
				dataManager.setDesignerInfo(designerInfo);
			}
		}
	}


	
	
}
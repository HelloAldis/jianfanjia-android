package com.jianfanjia.cn.http.request;

import android.content.Context;

import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.bean.DesignerInfo;
import com.jianfanjia.cn.config.Url;
import com.jianfanjia.cn.tools.JsonParser;

public class UserByDesignerInfoRequest extends BaseRequest{
	
	public UserByDesignerInfoRequest(Context context) {
		super(context);
		url = Url.GET_DESIGNER_INFO;
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
		if(data != null){
			DesignerInfo designerInfo = JsonParser.jsonToBean((String)data,
					DesignerInfo.class);
			if(designerInfo != null){
				dataManager.setDesignerInfo(designerInfo);
			}
		}
	}

	


}
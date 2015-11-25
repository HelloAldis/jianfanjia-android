package com.jianfanjia.cn.http.request;

import android.content.Context;

import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.bean.DesignerInfo;
import com.jianfanjia.cn.config.Url_New;
import com.jianfanjia.cn.tools.JsonParser;

public class UserByDesignerInfoRequest extends BaseRequest{
	
	public UserByDesignerInfoRequest(Context context) {
		super(context);
		url = Url_New.GET_DESIGNER_INFO;
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
		if(data.toString() != null){
			DesignerInfo designerInfo = JsonParser.jsonToBean(data.toString(),
					DesignerInfo.class);
			if(designerInfo != null){
				dataManager.setDesignerInfo(designerInfo);
			}
		}
	}

	


}

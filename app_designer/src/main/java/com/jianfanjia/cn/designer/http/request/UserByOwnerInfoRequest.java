package com.jianfanjia.cn.designer.http.request;

import android.content.Context;

import com.jianfanjia.cn.designer.base.BaseRequest;
import com.jianfanjia.cn.designer.bean.OwnerInfo;
import com.jianfanjia.cn.designer.tools.JsonParser;

public class UserByOwnerInfoRequest extends BaseRequest {

	public UserByOwnerInfoRequest(Context context) {
		super(context);
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
		if (data != null) {
			OwnerInfo ownerInfo = JsonParser.jsonToBean((String)data, OwnerInfo.class);
			if(ownerInfo != null){
				dataManager.setOwnerInfo(ownerInfo);
			}
		}
	}

}

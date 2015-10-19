package com.jianfanjia.cn.http.request;

import android.content.Context;
import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.bean.OwnerInfo;
import com.jianfanjia.cn.config.Url;
import com.jianfanjia.cn.tools.JsonParser;

public class UserByOwnerInfoRequest extends BaseRequest {

	public UserByOwnerInfoRequest(Context context) {
		super(context);
		url = Url.GET_OWER_INFO;
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

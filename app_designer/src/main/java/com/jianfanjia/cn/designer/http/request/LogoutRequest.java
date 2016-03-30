package com.jianfanjia.cn.designer.http.request;

import android.content.Context;

import com.jianfanjia.cn.designer.application.MyApplication;
import com.jianfanjia.cn.designer.base.BaseRequest;

public class LogoutRequest extends BaseRequest {

	public LogoutRequest(Context context) {
		super(context);
		url = url_new.SIGNOUT_URL;
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
		if (data != null) {
			dataManager.setLogin(false);
			dataManager.cleanData();
			MyApplication.getInstance().clearCookie();
		}
	}

}

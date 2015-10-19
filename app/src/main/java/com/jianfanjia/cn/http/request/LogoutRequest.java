package com.jianfanjia.cn.http.request;

import android.content.Context;

import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.config.Url_New;

public class LogoutRequest extends BaseRequest {

	public LogoutRequest(Context context) {
		super(context);
		url = Url_New.SIGNOUT_URL;
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

package com.jianfanjia.cn.http.request;

import android.content.Context;

import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.config.Url_New;

public class SendVerificationRequest extends BaseRequest {

	public SendVerificationRequest(Context context) {
		super(context);
		url = Url_New.GET_CODE_URL;
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
		if (data.toString() != null) {
			dataManager.setLogin(false);
			dataManager.cleanData();
			MyApplication.getInstance().clearCookie();
		}
	}

}

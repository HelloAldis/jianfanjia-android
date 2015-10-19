package com.jianfanjia.cn.http.request;

import android.content.Context;

import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.config.Url;

public class RegisterRequest extends BaseRequest {

	public RegisterRequest(Context context) {
		super(context);
		url = Url.REGISTER_URL;
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

}

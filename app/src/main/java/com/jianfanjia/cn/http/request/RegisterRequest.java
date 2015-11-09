package com.jianfanjia.cn.http.request;

import android.content.Context;

import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.bean.LoginUserBean;
import com.jianfanjia.cn.config.Url_New;
import com.jianfanjia.cn.tools.JsonParser;

import java.util.Calendar;

public class RegisterRequest extends BaseRequest {

	public RegisterRequest(Context context) {
		super(context);
		url = Url_New.REGISTER_URL;
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
			LoginUserBean loginUserBean = JsonParser.jsonToBean((String) data, LoginUserBean.class);
			dataManager.saveLoginUserInfo(loginUserBean);
			dataManager.setLogin(true);
			dataManager.savaLastLoginTime(Calendar.getInstance()
					.getTimeInMillis());
		}
	}

}

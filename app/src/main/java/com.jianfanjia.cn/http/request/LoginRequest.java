package com.jianfanjia.cn.http.request;

import java.util.Calendar;

import android.content.Context;

import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.base.BaseResponse;
import com.jianfanjia.cn.bean.LoginUserBean;

public class LoginRequest extends BaseRequest {
	private String username;
	private String password;

	public LoginRequest(Context context, String username, String password) {
		super(context);
		this.username = username;
		this.password = password;
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
		LoginUserBean loginUserBean = (LoginUserBean) baseResponse.getData();
		if (loginUserBean != null) {
			loginUserBean.setPass(password);
			dataManager.saveLoginUserInfo(loginUserBean);
			dataManager.setLogin(true);
			dataManager.savaLastLoginTime(Calendar.getInstance()
					.getTimeInMillis());
		}
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}

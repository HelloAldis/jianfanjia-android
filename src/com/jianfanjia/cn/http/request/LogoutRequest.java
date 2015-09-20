package com.jianfanjia.cn.http.request;

import java.util.Calendar;

import android.content.Context;

import com.jianfanjia.cn.activity.SettingActivity;
import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.base.BaseResponse;
import com.jianfanjia.cn.bean.LoginUserBean;
import com.jianfanjia.cn.cache.DataCleanManager;
import com.jianfanjia.cn.config.Constant;

public class LogoutRequest extends BaseRequest {
	
	public LogoutRequest(Context context) {
		super(context);
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
		if(baseResponse.getMsg() != null){
			dataManager.sharedPrefer.setValue(
					Constant.DESIGNER_PROCESS_LIST, null);
			 DataCleanManager.cleanSharedPafrenceByName(context,
			 Constant.SHARED_MAIN);// 清理掉用户相关的sharepre
			dataManager.setLogin(false);
			dataManager.cleanData();
			MyApplication.getInstance().clearCookie();
		}
	}

}

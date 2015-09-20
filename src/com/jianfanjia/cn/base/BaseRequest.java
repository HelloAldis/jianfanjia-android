package com.jianfanjia.cn.base;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.cache.DataManager;
import com.jianfanjia.cn.cache.DataManagerNew;
import com.jianfanjia.cn.view.dialog.DialogControl;
import com.jianfanjia.cn.view.dialog.DialogHelper;
import com.jianfanjia.cn.view.dialog.WaitDialog;

import android.content.Context;
import android.widget.Toast;

public class BaseRequest{
	
	protected DataManagerNew dataManager;
	protected Context context;
	
	public BaseRequest(Context context) {
		dataManager = DataManagerNew.getInstance();
		this.context = context;
	}

	//请求之前的准备操作
	protected void pre() {
		
	}
	
	//框架请求成功后的通用处理
	protected void all(){
		
	}
	
	//数据正确后的处理
	protected void onSuccess(BaseResponse baseResponse){
		
	}
	
	//数据错误后的处理
	public void onFailure(BaseResponse baseResponse){
		String err_msg = baseResponse.getErr_msg();
		if(err_msg != null){
			makeLongTextToast(err_msg);
		}
	}
	
	protected void makeLongTextToast(String message){
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}
	
	protected void makeShortTextToast(String message){
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}

}

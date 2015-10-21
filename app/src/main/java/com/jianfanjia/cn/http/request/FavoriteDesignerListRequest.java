package com.jianfanjia.cn.http.request;

import android.content.Context;

import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.config.Url_New;

public class FavoriteDesignerListRequest extends BaseRequest {


	public FavoriteDesignerListRequest(Context context) {
		super(context);
		url = Url_New.FAVORITE_DESIGNER_LIST;
	}
	
	@Override
	public void pre() {
		super.pre();
	}
	
	@Override
	public void onSuccess(Object data) {
		super.onSuccess(data);
		if(data != null){

		}
	}
	

}

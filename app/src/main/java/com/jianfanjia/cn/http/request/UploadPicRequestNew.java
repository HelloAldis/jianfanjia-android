package com.jianfanjia.cn.http.request;

import android.content.Context;
import android.text.TextUtils;
import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.config.Url;

public class UploadPicRequestNew extends BaseRequest {


	public UploadPicRequestNew(Context context) {
		super(context);
		url = Url.UPLOAD_IMAGE;
	}

	@Override
	public void all() {
		super.all();
	}

	@Override
	public void pre() {
		super.pre();
		dataManager.setCurrentUploadImageId(null);
	}

	@Override
	public void onSuccess(Object data) {
		if (data != null) {
			dataManager.setCurrentUploadImageId((String)data);
		}
	}


}

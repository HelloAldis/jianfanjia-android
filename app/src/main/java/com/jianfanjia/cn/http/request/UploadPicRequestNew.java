package com.jianfanjia.cn.http.request;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.base.BaseResponse;
import com.jianfanjia.cn.config.Url;
import com.squareup.okhttp.MediaType;

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
	public void onSuccess(BaseResponse baseResponse) {
		String data = baseResponse.getData().toString();
		if (!TextUtils.isEmpty(data)) {
			dataManager.setCurrentUploadImageId(data);
		}
	}


}

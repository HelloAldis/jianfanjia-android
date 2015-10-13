package com.jianfanjia.cn.http.request;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.base.BaseResponse;

public class UploadPicRequestNew extends BaseRequest {

	private Bitmap bitmap;

	public UploadPicRequestNew(Context context, Bitmap bitmap) {
		super(context);
		this.bitmap = bitmap;
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

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

}

package com.jianfanjia.cn.http.request;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.base.BaseResponse;
import com.jianfanjia.cn.tools.LogTool;

public class TotalDurationRequest extends BaseRequest {

	public TotalDurationRequest(Context context) {
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
		super.onSuccess(baseResponse);
		String data = baseResponse.getData().toString();
		if (data != null) {
			JSONObject jsonObject;
			try {
				jsonObject = new JSONObject(data);
				String duration = jsonObject.get("duration").toString();
				if(!TextUtils.isEmpty(duration)){
					dataManager.setTotalDuration(duration);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				LogTool.d(this.getClass().getName(), e.getMessage());
			}
		}
	}


}

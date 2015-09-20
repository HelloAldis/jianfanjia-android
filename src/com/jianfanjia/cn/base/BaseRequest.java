package com.jianfanjia.cn.base;

import android.content.Context;
import android.widget.Toast;

import com.jianfanjia.cn.cache.DataManagerNew;

public class BaseRequest {
	protected DataManagerNew dataManager;
	protected Context context;

	public BaseRequest(Context context) {
		dataManager = DataManagerNew.getInstance();
		this.context = context;
	}

	// ����֮ǰ��׼������
	protected void pre() {

	}

	// �������ɹ����ͨ�ô���
	protected void all() {

	}

	// ������ȷ��Ĵ���
	protected void onSuccess(BaseResponse baseResponse) {

	}

	// ���ݴ����Ĵ���
	public void onFailure(BaseResponse baseResponse) {
		String err_msg = baseResponse.getErr_msg();
		if (err_msg != null) {
			makeLongTextToast(err_msg);
		}
	}

	protected void makeLongTextToast(String message) {
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}

	protected void makeShortTextToast(String message) {
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}

}

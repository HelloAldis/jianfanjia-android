package com.jianfanjia.cn.interf;

import com.jianfanjia.cn.base.BaseResponse;

public interface LoadDataListener {
	void preLoad();
	
	void loadSuccess(BaseResponse baseResponse);

	void loadFailture();
}

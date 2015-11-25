package com.jianfanjia.cn.interf;

public interface ApiUiUpdateListener {
	void preLoad();
	
	void loadSuccess(Object data);

	void loadFailture(String errorMsg);
}

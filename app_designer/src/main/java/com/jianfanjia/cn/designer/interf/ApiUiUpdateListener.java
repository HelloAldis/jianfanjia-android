package com.jianfanjia.cn.designer.interf;

/**
 * Description: com.jianfanjia.cn.interf
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-10-17 20:18
 */
public interface ApiUiUpdateListener {
	void preLoad();
	
	void loadSuccess(Object data);

	void loadFailture(String error_msg);
}

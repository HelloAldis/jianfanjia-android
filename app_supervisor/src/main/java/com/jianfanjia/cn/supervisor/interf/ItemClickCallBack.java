package com.jianfanjia.cn.supervisor.interf;

import java.util.List;

public interface ItemClickCallBack {
	void click(int position, int itemType);

	void click(int position, int itemType, List<String> imageUrlList);
}

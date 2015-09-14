package com.jianfanjia.cn.interf;

import java.util.List;

public interface ItemClickCallBack {
	void click(int position, int itemType);

	void click(List<String> imageUrlList, int itemType);
}

package com.jianfanjia.cn.designer.ui.interf;

import java.util.List;

public interface ItemClickCallBack {
	void click(int position, int itemType);

	void click(int position, int itemType, List<String> imageUrlList);
}

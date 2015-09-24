package com.jianfanjia.cn.cache;

import com.jianfanjia.cn.config.Constant;

public class BusinessManager {
	
	public static int getCheckPicCountBySection(String section){
		if(section.equals(Constant.SHUI_DIAN)){
			return 6;
		}
		return 7;
	}

}

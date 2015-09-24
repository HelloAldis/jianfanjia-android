package com.jianfanjia.cn.cache;

import com.jianfanjia.cn.config.Constant;

public class BusinessManager {
	
	public static int getCheckPicCountBySection(String section){
		if(section.equals(Constant.SHUI_DIAN)){
			return 5;
		}else if(section.equals(Constant.NI_MU)){
			return 7;
		}else if(section.equals(Constant.YOU_QI)){
			return 2;
		}else if(section.equals(Constant.AN_ZHUANG)){
			return 1;
		}else if (section.equals(Constant.JUN_GONG)) {
			return 1;
		}
		return -1;
	}

}

package com.jianfanjia.cn.config;

import android.os.Environment;

/**
 * 
 * @ClassName: Constant
 * @Description: ������
 * @author fengliang
 * @date 2015-8-18 ����12:05:25
 * 
 */
public class Constant {
	public static final String SHARED_MAIN = "jianfanjia";
	public static final String LOG_PATH = Environment
			.getExternalStorageDirectory() + "/JianFanJia/MyLog/mylog.txt";// log���·��

	public static class Config {
		public static final boolean DEVELOPER_MODE = false;
	}

	public static final String ACCOUNT = "account";// �˺�
}

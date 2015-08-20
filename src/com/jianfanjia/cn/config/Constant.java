package com.jianfanjia.cn.config;

import android.os.Environment;

/**
 * 
 * @ClassName: Constant
 * @Description: 常量类
 * @author fengliang
 * @date 2015-8-18 下午12:05:25
 * 
 */
public class Constant {
	public static final String SHARED_MAIN = "jianfanjia";
	public static final String LOG_PATH = Environment
			.getExternalStorageDirectory() + "/JianFanJia/MyLog/mylog.txt";// log存放路径

	public static class Config {
		public static final boolean DEVELOPER_MODE = false;
	}

	public static final String ISFIRST = "isFirst";// 是否是首次打开程序
	public static final String ACCOUNT = "account";// 账号
	public static final String PASSWORD = "password";//密码
	public static final String USERTYPE = "usertype";//密码
	
	public static final String DATA = "data";//登录返回的数据key
	public static final String ERROR_MSG ="error_msg";//登录失败返回的信息

	
}

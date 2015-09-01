package com.jianfanjia.cn.config;

import java.util.HashMap;
import java.util.Map;

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

	// 用户身份
	public static final String IDENTITY_OWNER = "1";// 业主
	public static final String IDENTITY_DESIGNER = "2";// 设计师

	public static final String ISFIRST = "isFirst";// 是否是首次打开程序
	public static final String ACCOUNT = "account";// 账号
	public static final String PASSWORD = "password";// 密码
	public static final String USERTYPE = "usertype";// 用户类型
	public static final String USERIMAGE_ID = "userimageid";// 用户头像id

	public static final String DATA = "data";// 获取成功的数据
	public static final String ERROR_MSG = "err_msg";// 获取数据失败的返回信息
	public static final String SUCCESS_MSG = "msg";// 获取数据成功的返回信息

	// 提醒类型
	public static final int CAIGOU_NOTIFY = 1;// 采购提醒
	public static final int FUKUAN_NOTIFY = 2;// 付款提醒
	public static final int YANQI_NOTIFY = 3;// 延期提醒
	
	public static final int NOT_START=0;//未开工
	public static final int WORKING = 1;//正在进行中
	public static final int FINISH = 2;//已完工
	public static final int OWNER_APPLY_DELAY = 3;//业主申请延期中
	public static final int DESIGNER_APPLY_DELAY = 4;//设计师申请延期中
}

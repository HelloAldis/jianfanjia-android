package com.jianfanjia.cn.config;

import android.os.Environment;
import com.jianfanjia.cn.activity.R;

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
	public static final String ERROR_LOG_PATH = Environment
			.getExternalStorageDirectory() + "/JianFanJia/MyLog/errorlog.txt";// log存放路径

	public static final String COMMON_PATH = Environment
			.getExternalStorageDirectory() + "/JianFanJia/";// 公共的路径

	public static final String PROCESSINFO_CACHE = "processinfo_cache";// 工地信息缓存文件名
	public static final String DESIGNERINFO_CACHE = "designerinfo_cache";// 设计师信息缓存文件名

	public static class Config {
		public static final boolean DEVELOPER_MODE = false;
	}

	public static final int REQUESTCODE_CAMERA = 1;// 拍照
	public static final int REQUESTCODE__LOCATION = 2;// 本地相册选取
	public static final int REQUESTCODE__CROP = 3;// 裁剪
	// 侧滑菜单
	public static final int HOME = 0;
	public static final int NOTIFY = 1;
	public static final int MY = 2;
	public static final int MYSITE = 3;
	public static final int SETTING = 4;

	// 用户身份
	public static final String IDENTITY_OWNER = "1";// 业主
	public static final String IDENTITY_DESIGNER = "2";// 设计师

	public static final String DESIGNER_SITE_ID = "siteId";// 设计师当前切换工地id
	public static final String ISOPEN = "isOpen";// 消息推送是否开启
	public static final String ISFIRST = "isFirst";// 是否是首次打开程序
	public static final String ACCOUNT = "account";// 账号
	public static final String PASSWORD = "password";// 密码
	public static final String USERNAME = "username";// 用户名称
	public static final String USERTYPE = "usertype";// 用户类型
	public static final String USERIMAGE_ID = "userimageid";// 用户头像id

	public static final String FINAL_DESIGNER_ID = "final_designerid";// 业主的设计师id
	public static final String FINAL_OWNER_ID = "final_owner_id";// 业主id
	public static final String PROCESSINFO_ID = "processinfo_id";// 当前工地id
	public static final String PROCESSINFO_LIST = "processinfo_list";// 当前工地id
	public static final String OWNER_INFO = "owner_info";// 当前业主信息
	public static final String DESIGNER_INFO = "designer_info";// 当前设计师信息
	


	public static final String DATA = "data";// 获取成功的数据
	public static final String ERROR_MSG = "err_msg";// 获取数据失败的返回信息
	public static final String SUCCESS_MSG = "msg";// 获取数据成功的返回信息

	// 提醒类型
	public static final int CAIGOU_NOTIFY = 1;// 采购提醒
	public static final int FUKUAN_NOTIFY = 2;// 付款提醒
	public static final int YANQI_NOTIFY = 3;// 延期提醒

	public static final int NOT_START = 0;// 未开工
	public static final int WORKING = 1;// 正在进行中
	public static final int FINISH = 2;// 已完工
	public static final int OWNER_APPLY_DELAY = 3;// 业主申请延期中
	public static final int DESIGNER_APPLY_DELAY = 4;// 设计师申请延期中

	public static final String SEX_MAN = "0";
	public static final String SEX_WOMEN = "1";

	public static final String DESIGNER_NOT_AUTH_TYPE = "0";// 设计师未认证
	public static final String DESIGNER_NOW_AUTH_TYPE = "1";// 设计师正在审核中
	public static final String DESIGNER_FINISH_AUTH_TYPE = "2";// 设计师完成认证

	public static final String HOME_ADD_PIC = "drawable://"
			+ R.drawable.btn_icon_home_add;

	public static final String DEFALUT_OWNER_PIC = "drawable://"
			+ R.drawable.icon_sidebar_default_user;

	public static final String DEFALUT_DESIGNER_PIC = "drawable://"
			+ R.drawable.icon_sidebar_default_user;

	public static final String CURRENT_LIST = "current_list";// 首页展开的第一道工序
	public static final String CURRENT_ITEM = "current_item";// 当前节点

	// editactivity
	public static final int REQUESTCODE_EDIT_USERNAME = 0;
	public static final int REQUESTCODE_EDIT_ADDRESS = 1;
	public static final String EDIT_TYPE = "inputType";
	public static final String EDIT_CONTENT = "content";

}

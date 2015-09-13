package com.jianfanjia.cn.config;

/**
 * 
 * @ClassName: Url
 * @Description:服务器地址及相关接口类
 * @author fengliang
 * @date 2015-8-18 下午12:06:07
 * 
 */
public class Url {
	public static String SEVER_IP = "101.200.191.159";
	// public static String SEVER_IP = "192.168.1.107";

	public static String SEVER_PORT = "80";

	public static final String HTTPROOT = "http://" + SEVER_IP + ":"
			+ SEVER_PORT + "/api/v1/";

	public static final String ID = "id";
	//
	public static final String BIND_URL = HTTPROOT + "device/bind";
	// 登录
	public static final String LOGIN_URL = HTTPROOT + "login";
	// 注册
	public static final String REGISTER_URL = HTTPROOT + "signup";
	// 获取短信验证码
	public static final String GET_CODE_URL = HTTPROOT + "send_verify_code";
	// 忘记密码
	public static final String UPDATE_PASS_URL = HTTPROOT + "update_pass";
	// 登出
	public static final String SIGNOUT_URL = HTTPROOT + "signout";
	// 检查版本
	public static final String UPDATE_VERSION_URL = HTTPROOT
			+ "device/android_build_version";
	// --------------------------------------------------业主-----------------------------------------------------------
	// 业主获取需求
	public static final String REQUIREMENT = HTTPROOT + "user/requirement";
	// 业主发送工地配置和配置工地
	public static final String PROCESS = HTTPROOT + "user/process";
	// 获取业主的设计师
	public static final String GET_OWER_DESIGNER = HTTPROOT + "designer/" + ID
			+ "/basicinfo";
	// 业主获取自己的个人资料
	public static final String GET_OWER_INFO = HTTPROOT + "user/info";
	// ----------------------------------------------------设计师----------------------------------------------------------
	// 设计师获取个人信息
	public static final String GET_DESIGNER_INFO = HTTPROOT + "designer/info";
	// 获取设计师的业主
	public static final String GET_DESIGNER_OWNER = HTTPROOT + "designer/user";
	// 获取设计师的工地列表
	public static final String GET_DESIGNER_PROCESS = HTTPROOT
			+ "/process/list";
	// 设计师获取我的方案
	public static final String GET_DESIGNER_PLAN = HTTPROOT + "designer/plan";
	// 设计师提交方案
	public static final String SUBMIT_DESIGNER_PLAN = HTTPROOT
			+ "designer/plan";
	// 用户上传图片
	public static final String UPLOAD_IMAGE = HTTPROOT + "image/upload";
	// 设计师提交验收图片
	public static final String SUBMIT_YAHSHOU_IMAGE = HTTPROOT
			+ "process/ysimage";
	// 设计师删除验收图片
	public static final String DELETE_YAHSHOU_IMAGE = HTTPROOT
			+ "process/ysimage";
	// 用户上传图片到装修流程
	public static final String POST_PROCESS_IMAGE = HTTPROOT + "process/image";
	// 评价装修流程
	public static final String POST_PROCESS_COMMENT = HTTPROOT
			+ "process/comment";
	// 用户获取某个装修流程
	public static final String GET_ONE_PROCESS = HTTPROOT + "process/";
	// 用户获取业主个人信息
	public static final String GET_ONE_OWNER_INFO = HTTPROOT + "user/" + ID
			+ "/info";
	// 用户同意改期
	public static final String AGREE_RESCHDULE = HTTPROOT
			+ "process/reschedule/ok";
	// 用户拒绝改期
	public static final String REFUSE_RESCHDULE = HTTPROOT
			+ "process/reschedule/reject";
	// 用户获取我的改期提醒
	public static final String GET_RESCHDULE_ALL = HTTPROOT
			+ "process/reschedule/all";
	// 获取图片
	public static final String GET_IMAGE = HTTPROOT + "image/";
	// 根据工地id获取某个工地
	public static final String GET_PROCESSINFO_BYID = HTTPROOT + "process/"
			+ ID;
	// 用户完工装修流程小节点
	public static final String POST_PROCESS_DONE_ITEM = HTTPROOT
			+ "process/done_item";
}

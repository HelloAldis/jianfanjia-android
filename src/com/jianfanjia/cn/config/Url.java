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
	public static String SEVER_IP = "192.168.1.107";
	public static String SEVER_PORT = "80";
	public static final String HTTPROOT = "http://" + SEVER_IP + ":"
			+ SEVER_PORT + "/api/v1/";
	// 登录
	public static final String LOGIN_URL = HTTPROOT + "login";
	// 注册
	public static final String REGISTER_URL = HTTPROOT + "signup";
	// 获取短信验证码
	public static final String GET_CODE_URL = HTTPROOT + "send_verify_code";
}

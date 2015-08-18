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
	public static String SEVER_IP = "120.55.103.141";
	public static String SEVER_PORT = "80";
	public static final String HTTPROOT = "http://" + SEVER_IP + ":"
			+ SEVER_PORT + "/HomeApi/";

	public static final String LOGIN_URL = HTTPROOT + "Login";
}

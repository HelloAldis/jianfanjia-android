package com.jianfanjia.cn.config;

/**
 * 
 * @ClassName: Url
 * @Description:��������ַ����ؽӿ���
 * @author fengliang
 * @date 2015-8-18 ����12:06:07
 * 
 */
public class Url {
	public static String SEVER_IP = "192.168.1.107";
	public static String SEVER_PORT = "80";
	public static final String HTTPROOT = "http://" + SEVER_IP + ":"
			+ SEVER_PORT + "/api/v1/";
	// ��¼
	public static final String LOGIN_URL = HTTPROOT + "login";
	// ע��
	public static final String REGISTER_URL = HTTPROOT + "signup";
	// ��ȡ������֤��
	public static final String GET_CODE_URL = HTTPROOT + "send_verify_code";
}

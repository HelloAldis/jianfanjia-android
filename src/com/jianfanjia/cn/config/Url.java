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
	public static String SEVER_IP = "120.55.103.141";
	public static String SEVER_PORT = "80";
	public static final String HTTPROOT = "http://" + SEVER_IP + ":"
			+ SEVER_PORT + "/HomeApi/";
	// ��¼
	public static final String LOGIN_URL = HTTPROOT + "Login";
	// ע��
	public static final String REGISTER_URL = HTTPROOT + "Register";
	// ��ȡ������֤��
	public static final String GET_CODE_URL = HTTPROOT + "GetCode";
}

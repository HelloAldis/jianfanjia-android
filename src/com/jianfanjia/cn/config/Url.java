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
	// ��������
	public static final String UPDATE_PASS_URL = HTTPROOT + "update_pass";
	// �ǳ�
	public static final String SIGNOUT_URL = HTTPROOT + "signout";
	// ҵ����ȡ����
	public static final String REQUIREMENT = HTTPROOT + "user/requirement";
	// ҵ�����͹������ú����ù���
	public static final String PROCESS = HTTPROOT + "user/process";
	// ��ȡҵ�������ʦ
	public static final String GET_OWER_DESIGNER = "user/designer";
	// ��ȡҵ���ĸ�������
	public static final String GET_OWER_INFO = "user/info";
	// ��ȡ���ʦ�ĸ�������
	public static final String GET_DESIGNER_INFO = "designer/info";
	// ��ȡ���ʦ��ҵ��
	public static final String GET_DESIGNER_OWNER = "designer/user";
	// ��ȡ���ʦ�Ĺ����б�
	public static final String GET_DESIGNER_PROCESS = "designer/process/list";

}

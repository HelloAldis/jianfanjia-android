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
	// public static String SEVER_IP = "www.jianfanjia.com";
	public static String SEVER_IP = "192.168.1.107";

	public static String SEVER_PORT = "80";

	public static final String HTTPROOT = "http://" + SEVER_IP + ":"
			+ SEVER_PORT + "/api/v1/";

	public static final String IMGROOT = "http://" + SEVER_IP + ":"
			+ SEVER_PORT + "/api/v1/image/";

	public static final String ID = "id";
	//
	public static final String BIND_URL = HTTPROOT + "device/bind";
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
	// ���汾
	public static final String UPDATE_VERSION_URL = HTTPROOT
			+ "device/android_build_version";
	// --------------------------------------------------ҵ��-----------------------------------------------------------
	// ҵ����ȡ����
	public static final String REQUIREMENT = HTTPROOT + "user/requirement";
	// ҵ�����͹������ú����ù���
	public static final String PROCESS = HTTPROOT + "user/process";
	// ��ȡҵ�������ʦ
	public static final String GET_OWER_DESIGNER = HTTPROOT + "designer/" + ID
			+ "/basicinfo";
	// ҵ����ȡ�Լ��ĸ�������
	public static final String GET_OWER_INFO = HTTPROOT + "user/info";
	// ----------------------------------------------------���ʦ----------------------------------------------------------
	// ���ʦ��ȡ������Ϣ
	public static final String GET_DESIGNER_INFO = HTTPROOT + "designer/info";
	// ��ȡ���ʦ��ҵ��
	public static final String GET_DESIGNER_OWNER = HTTPROOT + "designer/user";
	// ��ȡ���ʦ�Ĺ����б�
	public static final String GET_DESIGNER_PROCESS = HTTPROOT
			+ "designer/process/list";
	// ���ʦ��ȡ�ҵķ���
	public static final String GET_DESIGNER_PLAN = HTTPROOT + "designer/plan";
	// ���ʦ�ύ����
	public static final String SUBMIT_DESIGNER_PLAN = HTTPROOT
			+ "designer/plan";
	// �ϴ�ͼƬ��װ������
	public static final String POST_PROCESS_IMAGE = HTTPROOT + "process/image";
	// ����װ������
	public static final String POST_PROCESS_COMMENT = HTTPROOT
			+ "process/comment";

	// ��ȡͼƬ
	public static final String GET_IMAGE = HTTPROOT + "image/";

}

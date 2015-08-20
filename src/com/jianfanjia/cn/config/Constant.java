package com.jianfanjia.cn.config;

import android.os.Environment;

/**
 * 
 * @ClassName: Constant
 * @Description: ������
 * @author fengliang
 * @date 2015-8-18 ����12:05:25
 * 
 */
public class Constant {
	public static final String SHARED_MAIN = "jianfanjia";
	public static final String LOG_PATH = Environment
			.getExternalStorageDirectory() + "/JianFanJia/MyLog/mylog.txt";// log���·��

	public static class Config {
		public static final boolean DEVELOPER_MODE = false;
	}

	public static final String ISFIRST = "isFirst";// �Ƿ����״δ򿪳���
	public static final String ACCOUNT = "account";// �˺�
	public static final String PASSWORD = "password";//����
	public static final String USERTYPE = "usertype";//����
	
	public static final String DATA = "data";//��¼���ص�����key
	public static final String ERROR_MSG ="error_msg";//��¼ʧ�ܷ��ص���Ϣ

	
}

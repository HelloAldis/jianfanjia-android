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
	
	public static final String DATA = "data";//��ȡ�ɹ�������
	public static final String ERROR_MSG ="err_msg";//��ȡ����ʧ�ܵķ�����Ϣ
	public static final String SUCCESS_MSG ="msg";//��ȡ���ݳɹ��ķ�����Ϣ

	
}

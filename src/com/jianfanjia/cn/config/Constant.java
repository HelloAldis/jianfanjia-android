package com.jianfanjia.cn.config;

import java.util.HashMap;
import java.util.Map;

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

	// �û����
	public static final String IDENTITY_OWNER = "1";// ҵ��
	public static final String IDENTITY_DESIGNER = "2";// ���ʦ

	public static final String ISFIRST = "isFirst";// �Ƿ����״δ򿪳���
	public static final String ACCOUNT = "account";// �˺�
	public static final String PASSWORD = "password";// ����
	public static final String USERTYPE = "usertype";// �û�����
	public static final String USERIMAGE_ID = "userimageid";// �û�ͷ��id

	public static final String DATA = "data";// ��ȡ�ɹ�������
	public static final String ERROR_MSG = "err_msg";// ��ȡ����ʧ�ܵķ�����Ϣ
	public static final String SUCCESS_MSG = "msg";// ��ȡ���ݳɹ��ķ�����Ϣ

	// ��������
	public static final int CAIGOU_NOTIFY = 1;// �ɹ�����
	public static final int FUKUAN_NOTIFY = 2;// ��������
	public static final int YANQI_NOTIFY = 3;// ��������
	
	public static final int NOT_START=0;//δ����
	public static final int WORKING = 1;//���ڽ�����
	public static final int FINISH = 2;//���깤
	public static final int OWNER_APPLY_DELAY = 3;//ҵ������������
	public static final int DESIGNER_APPLY_DELAY = 4;//���ʦ����������
}

package com.jianfanjia.cn.config;

import android.os.Environment;
import com.jianfanjia.cn.activity.R;

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
	public static final String ERROR_LOG_PATH = Environment
			.getExternalStorageDirectory() + "/JianFanJia/MyLog/errorlog.txt";// log���·��

	public static final String COMMON_PATH = Environment
			.getExternalStorageDirectory() + "/JianFanJia/";// ������·��

	public static final String PROCESSINFO_CACHE = "processinfo_cache";// ������Ϣ�����ļ���
	public static final String DESIGNERINFO_CACHE = "designerinfo_cache";// ���ʦ��Ϣ�����ļ���

	public static class Config {
		public static final boolean DEVELOPER_MODE = false;
	}

	public static final int REQUESTCODE_CAMERA = 1;// ����
	public static final int REQUESTCODE__LOCATION = 2;// �������ѡȡ
	public static final int REQUESTCODE__CROP = 3;// �ü�
	// �໬�˵�
	public static final int HOME = 0;
	public static final int NOTIFY = 1;
	public static final int MY = 2;
	public static final int MYSITE = 3;
	public static final int SETTING = 4;

	// �û����
	public static final String IDENTITY_OWNER = "1";// ҵ��
	public static final String IDENTITY_DESIGNER = "2";// ���ʦ

	public static final String DESIGNER_SITE_ID = "siteId";// ���ʦ��ǰ�л�����id
	public static final String ISOPEN = "isOpen";// ��Ϣ�����Ƿ���
	public static final String ISFIRST = "isFirst";// �Ƿ����״δ򿪳���
	public static final String ACCOUNT = "account";// �˺�
	public static final String PASSWORD = "password";// ����
	public static final String USERNAME = "username";// �û�����
	public static final String USERTYPE = "usertype";// �û�����
	public static final String USERIMAGE_ID = "userimageid";// �û�ͷ��id

	public static final String FINAL_DESIGNER_ID = "final_designerid";// ҵ�������ʦid
	public static final String FINAL_OWNER_ID = "final_owner_id";// ҵ��id
	public static final String PROCESSINFO_ID = "processinfo_id";// ��ǰ����id
	public static final String PROCESSINFO_LIST = "processinfo_list";// ��ǰ����id
	public static final String OWNER_INFO = "owner_info";// ��ǰҵ����Ϣ
	public static final String DESIGNER_INFO = "designer_info";// ��ǰ���ʦ��Ϣ
	


	public static final String DATA = "data";// ��ȡ�ɹ�������
	public static final String ERROR_MSG = "err_msg";// ��ȡ����ʧ�ܵķ�����Ϣ
	public static final String SUCCESS_MSG = "msg";// ��ȡ���ݳɹ��ķ�����Ϣ

	// ��������
	public static final int CAIGOU_NOTIFY = 1;// �ɹ�����
	public static final int FUKUAN_NOTIFY = 2;// ��������
	public static final int YANQI_NOTIFY = 3;// ��������

	public static final int NOT_START = 0;// δ����
	public static final int WORKING = 1;// ���ڽ�����
	public static final int FINISH = 2;// ���깤
	public static final int OWNER_APPLY_DELAY = 3;// ҵ������������
	public static final int DESIGNER_APPLY_DELAY = 4;// ���ʦ����������

	public static final String SEX_MAN = "0";
	public static final String SEX_WOMEN = "1";

	public static final String DESIGNER_NOT_AUTH_TYPE = "0";// ���ʦδ��֤
	public static final String DESIGNER_NOW_AUTH_TYPE = "1";// ���ʦ���������
	public static final String DESIGNER_FINISH_AUTH_TYPE = "2";// ���ʦ�����֤

	public static final String HOME_ADD_PIC = "drawable://"
			+ R.drawable.btn_icon_home_add;

	public static final String DEFALUT_OWNER_PIC = "drawable://"
			+ R.drawable.icon_sidebar_default_user;

	public static final String DEFALUT_DESIGNER_PIC = "drawable://"
			+ R.drawable.icon_sidebar_default_user;

	public static final String CURRENT_LIST = "current_list";// ��ҳչ���ĵ�һ������
	public static final String CURRENT_ITEM = "current_item";// ��ǰ�ڵ�

	// editactivity
	public static final int REQUESTCODE_EDIT_USERNAME = 0;
	public static final int REQUESTCODE_EDIT_ADDRESS = 1;
	public static final String EDIT_TYPE = "inputType";
	public static final String EDIT_CONTENT = "content";

}

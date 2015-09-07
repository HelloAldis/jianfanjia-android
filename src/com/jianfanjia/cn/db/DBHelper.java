package com.jianfanjia.cn.db;

import android.content.Context;
import com.css.cn.util.MyDBHelper;
import com.jianfanjia.cn.bean.NotifyCaiGouInfo;
import com.jianfanjia.cn.bean.NotifyDelayInfo;
import com.jianfanjia.cn.bean.NotifyPayInfo;

/**
 * 
 * @ClassName: DBHelper
 * @Description: 数据库类
 * @author fengliang
 * @date 2015-8-18 下午3:09:13
 * 
 */
public class DBHelper extends MyDBHelper {
	private static final String DBNAME = "JIANFANJIA.db";
	private static final int DBVERSION = 1;
	private static final Class<?>[] clazz = { NotifyCaiGouInfo.class,
			NotifyPayInfo.class, NotifyDelayInfo.class };

	public DBHelper(Context context) {
		super(context, DBNAME, null, DBVERSION, clazz);
	}
}

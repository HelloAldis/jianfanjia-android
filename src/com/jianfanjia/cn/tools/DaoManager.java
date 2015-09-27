package com.jianfanjia.cn.tools;

import android.content.Context;
import com.jianfanjia.cn.dao.NotifyMessageDao;

/**
 * 
 * @ClassName: DaoManager
 * @Description: TODO
 * @author fengliang
 * @date 2015��9��27�� ����8:55:10
 *
 */
public class DaoManager {
	private static NotifyMessageDao notifyMessageDao = null;

	public synchronized static NotifyMessageDao getNotifyMessageDao(
			Context context) {
		if (null == notifyMessageDao) {
			notifyMessageDao = new NotifyMessageDao(context);
		}
		return notifyMessageDao;
	}
}

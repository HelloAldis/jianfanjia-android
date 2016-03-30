package com.jianfanjia.cn.tools;

import android.content.Context;

import com.jianfanjia.cn.dao.impl.NotifyMessageDao;

/**
 * @author fengliang
 * @ClassName: DaoManager
 * @Description: TODO
 * @date 2015年9月27日 下午8:55:10
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

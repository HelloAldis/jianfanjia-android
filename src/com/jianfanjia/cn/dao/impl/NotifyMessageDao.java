package com.jianfanjia.cn.dao.impl;

import java.sql.SQLException;
import java.util.List;
import android.content.Context;
import com.jianfanjia.cn.bean.NotifyMessage;
import com.jianfanjia.cn.dao.INotifyMessageDao;

/**
 * 
 * @ClassName: NotifyMessageDao
 * @Description: TODO
 * @author fengliang
 * @date 2015��9��27�� ����9:28:54
 * 
 */
public class NotifyMessageDao extends BaseDaoImpl<NotifyMessage> implements
		INotifyMessageDao {

	public NotifyMessageDao(Context context) {
		super(context, NotifyMessage.class);
	}

	@Override
	public List<NotifyMessage> getNotifyListByType(String type) {
		List<NotifyMessage> list = null;
		try {
			list = dao.queryBuilder().orderBy("time", false).where()
					.eq("type", type).query();
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}

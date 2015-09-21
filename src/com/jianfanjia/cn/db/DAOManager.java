package com.jianfanjia.cn.db;

import java.sql.SQLException;
import java.util.List;
import android.content.Context;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.jianfanjia.cn.bean.NotifyMessage;

/**
 * 
 * @ClassName: DAOManager
 * @Description: DAOManager
 * @author fengliang
 * @date 2015-9-21 ����3:01:12
 * 
 */
public class DAOManager {
	private static DAOManager manager = null;
	private DBHelper helper;
	private Dao<NotifyMessage, Integer> dao;

	public DAOManager(Context context) {
		helper = OpenHelperManager.getHelper(context, DBHelper.class);
		dao = helper.getHelperDao();
	}

	/**
	 * ��ȡ������
	 * 
	 * @param context
	 * @return
	 */
	public static DAOManager getInstance(Context context) {
		if (manager == null) {
			manager = new DAOManager(context);
		}
		return manager;
	}

	/**
	 * �رչ�����
	 */
	public void close() {
		if (helper != null) {
			helper.close();
			OpenHelperManager.releaseHelper();
			helper = null;
		}
	}

	/**
	 * ��
	 * 
	 * @param notifys
	 * @throws SQLException
	 */
	public void add(List<NotifyMessage> notifys) throws SQLException {
		if (notifys != null) {
			for (NotifyMessage message : notifys) {
				dao.create(message);
			}
		}
	}

	/**
	 * ��
	 * 
	 * @param message
	 * @throws SQLException
	 */
	public void add(NotifyMessage message) throws SQLException {
		if (message != null) {
			dao.create(message);
		}
	}

	/**
	 * ɾ
	 * 
	 * @param message
	 * @throws SQLException
	 */
	public void delete(NotifyMessage message) throws SQLException {
		if (message != null) {
			dao.delete(message);
		}
	}

	/**
	 * ��
	 * 
	 * @param message
	 * @throws SQLException
	 */
	public void update(NotifyMessage message) throws SQLException {
		if (message != null) {
			dao.update(message);
		}
	}

	/**
	 * ��
	 * 
	 * @return
	 * @throws SQLException
	 */
	public List<NotifyMessage> quary() throws SQLException {
		List<NotifyMessage> list = null;
		list = dao.queryForAll();
		return list;
	}
}

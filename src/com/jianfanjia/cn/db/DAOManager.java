package com.jianfanjia.cn.db;

import java.sql.SQLException;
import java.util.List;
import android.content.Context;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.jianfanjia.cn.bean.NotifyCaiGouInfo;

/**
 * 
 * @ClassName: DAOManager
 * @Description: DAOManager
 * @author fengliang
 * @date 2015-9-21 下午3:01:12
 * 
 */
public class DAOManager {
	private static DAOManager manager = null;
	private DBHelper helper;
	private Dao<NotifyCaiGouInfo, Integer> dao;

	public DAOManager(Context context) {
		helper = OpenHelperManager.getHelper(context, DBHelper.class);
		dao = helper.getHelperDao();
	}

	/**
	 * 获取管理器
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
	 * 关闭管理器
	 */
	public void close() {
		if (helper != null) {
			helper.close();
			OpenHelperManager.releaseHelper();
			helper = null;
		}
	}

	/**
	 * 增
	 * 
	 * @param notifys
	 * @throws SQLException
	 */
	public void add(List<NotifyCaiGouInfo> notifys) throws SQLException {
		if (notifys != null) {
			for (NotifyCaiGouInfo caigouInfo : notifys) {
				dao.create(caigouInfo);
			}
		}
	}

	/**
	 * 增
	 * 
	 * @param caigouInfo
	 * @throws SQLException
	 */
	public void add(NotifyCaiGouInfo caigouInfo) throws SQLException {
		if (caigouInfo != null) {
			dao.create(caigouInfo);
		}
	}

	/**
	 * 删
	 * 
	 * @param caigouInfo
	 * @throws SQLException
	 */
	public void delete(NotifyCaiGouInfo caigouInfo) throws SQLException {
		if (caigouInfo != null) {
			dao.delete(caigouInfo);
		}
	}

	/**
	 * 改
	 * 
	 * @param caigouInfo
	 * @throws SQLException
	 */
	public void update(NotifyCaiGouInfo caigouInfo) throws SQLException {
		if (caigouInfo != null) {
			dao.update(caigouInfo);
		}
	}

	/**
	 * 查
	 * 
	 * @return
	 * @throws SQLException
	 */
	public List<NotifyCaiGouInfo> quary() throws SQLException {
		List<NotifyCaiGouInfo> list = null;
		list = dao.queryForAll();
		return list;
	}
}

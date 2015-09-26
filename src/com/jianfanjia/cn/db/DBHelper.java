package com.jianfanjia.cn.db;

import java.sql.SQLException;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.jianfanjia.cn.bean.NotifyMessage;

/**
 * 
 * @ClassName: DBHelper
 * @Description: 数据库类
 * @author fengliang
 * @date 2015-8-18 下午3:09:13
 * 
 */
public class DBHelper extends OrmLiteSqliteOpenHelper {
	private static final String DBNAME = "JIANFANJIA.db";
	private static final int DBVERSION = 1;
	private Dao<NotifyMessage, Integer> dao;

	public DBHelper(Context context) {
		super(context, DBNAME, null, DBVERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			TableUtils.createTable(connectionSource, NotifyMessage.class);
			// TableUtils.createTable(connectionSource, NotifyDelayInfo.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
			int arg2, int arg3) {
		try {
			TableUtils.dropTable(connectionSource, NotifyMessage.class, true);
			// TableUtils.dropTable(connectionSource, NotifyDelayInfo.class,
			// true);
			onCreate(db, connectionSource);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void close() {
		super.close();
		dao = null;
	}

	/**
	 * 获取Dao操作类
	 * 
	 * @return
	 */
	public Dao<NotifyMessage, Integer> getHelperDao() {
		if (dao == null) {
			try {
				dao = getDao(NotifyMessage.class);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return dao;
	}
}

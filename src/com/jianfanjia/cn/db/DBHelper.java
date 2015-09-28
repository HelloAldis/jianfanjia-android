package com.jianfanjia.cn.db;

import java.sql.SQLException;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
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
	private static DBHelper instance;

	public DBHelper(Context context) {
		super(context, DBNAME, null, DBVERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			TableUtils.createTable(connectionSource, NotifyMessage.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
			int arg2, int arg3) {
		try {
			TableUtils.dropTable(connectionSource, NotifyMessage.class, true);
			onCreate(db, connectionSource);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static synchronized DBHelper getHelper(Context context) {
		if (instance == null) {
			synchronized (DBHelper.class) {
				if (instance == null)
					instance = new DBHelper(context);
			}
		}
		return instance;
	}

	@Override
	public void close() {
		super.close();
	}

}

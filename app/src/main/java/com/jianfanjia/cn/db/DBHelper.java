package com.jianfanjia.cn.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.jianfanjia.cn.bean.NotifyMessage;

import java.sql.SQLException;

/**
 * Description:数据库类
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 16:10
 */
public class DBHelper extends OrmLiteSqliteOpenHelper {
    private static final String DBNAME = "jianfanjia.db";
    private static final int DBVERSION = 1;
    private static DBHelper helper;

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
        if (helper == null) {
            synchronized (DBHelper.class) {
                if (helper == null)
                    helper = new DBHelper(context);
            }
        }
        return helper;
    }

    @Override
    public void close() {
        super.close();
        helper = null;
    }

}

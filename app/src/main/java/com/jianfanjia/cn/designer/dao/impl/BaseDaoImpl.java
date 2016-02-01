package com.jianfanjia.cn.designer.dao.impl;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.jianfanjia.cn.designer.dao.BaseDao;
import com.jianfanjia.cn.designer.db.DBHelper;
import com.jianfanjia.cn.designer.tools.LogTool;

import java.sql.SQLException;
import java.util.List;

public class BaseDaoImpl<T> implements BaseDao<T> {
    private static final String TAG = BaseDaoImpl.class.getName();
    protected Dao<T, Integer> dao;

    public BaseDaoImpl(Context context, Class<T> clazz) {
        try {
            dao = DBHelper.getHelper(context).getDao(clazz);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean save(T t) {
        boolean flag = false;
        try {
            int result = dao.create(t);
            LogTool.d(TAG, "save() result" + result);
            if (result > 0) {
                flag = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }

    @Override
    public void save(List<T> list) {
        try {
            for (T t : list) {
                dao.create(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean delete(T t) {
        boolean flag = false;
        try {
            int result = dao.delete(t);
            LogTool.d(TAG, "delete() result" + result);
            if (result > 0) {
                flag = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }

    @Override
    public boolean update(T t) {
        boolean flag = false;
        try {
            int result = dao.update(t);
            LogTool.d(TAG, "update() result" + result);
            if (result > 0) {
                flag = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }

    @Override
    public List<T> query() {
        List<T> list = null;
        try {
            list = dao.queryForAll();
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public T findOneById(int id) {
        T t = null;
        try {
            t = dao.queryForId(id);
            return t;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}

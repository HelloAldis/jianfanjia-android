package com.jianfanjia.cn.designer.dao.impl;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.jianfanjia.cn.designer.dao.BaseDao;
import com.jianfanjia.cn.designer.db.DBHelper;

import java.sql.SQLException;
import java.util.List;

public class BaseDaoImpl<T> implements BaseDao<T> {
	protected Dao<T, Integer> dao;

	public BaseDaoImpl(Context context, Class<T> clazz) {
		try {
			dao = DBHelper.getHelper(context).getDao(clazz);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void save(T t) {
		try {
			dao.create(t);
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
	public void delete(T t) {
		try {
			dao.delete(t);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(T t) {
		try {
			dao.update(t);
		} catch (SQLException e) {
			e.printStackTrace();
		}
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

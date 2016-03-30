package com.jianfanjia.cn.dao;

import java.util.List;

public interface BaseDao<T> {
    boolean save(T t);

    void save(List<T> list);

    boolean delete(T t);

    boolean update(T t);

    List<T> query();

    T findOneById(int id);
}

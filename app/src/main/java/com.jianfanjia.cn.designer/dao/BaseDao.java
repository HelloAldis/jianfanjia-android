package com.jianfanjia.cn.designer.dao;

import java.util.List;

public interface BaseDao<T> {
    public abstract void save(T t);

    public abstract void save(List<T> list);

    public abstract void delete(T t);

    public abstract void update(T t);

    public abstract List<T> query();

    public abstract T findOneById(int id);
}

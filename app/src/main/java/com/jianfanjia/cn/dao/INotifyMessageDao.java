package com.jianfanjia.cn.dao;

import com.jianfanjia.cn.bean.NotifyMessage;

import java.util.List;

public interface INotifyMessageDao extends BaseDao<NotifyMessage> {
    public List<NotifyMessage> getNotifyListByType(String type, String userid);
}

package com.jianfanjia.cn.designer.dao;

import com.jianfanjia.cn.designer.bean.NotifyMessage;

import java.util.List;

public interface INotifyMessageDao extends BaseDao<NotifyMessage> {
    public List<NotifyMessage> getNotifyListByType(String type, String userid);
}

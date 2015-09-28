package com.jianfanjia.cn.dao;

import java.util.List;
import com.jianfanjia.cn.bean.NotifyMessage;

public interface INotifyMessageDao extends BaseDao<NotifyMessage> {
	public List<NotifyMessage> getNotifyListByType(String type);
}

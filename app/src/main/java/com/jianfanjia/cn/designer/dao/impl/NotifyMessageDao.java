package com.jianfanjia.cn.designer.dao.impl;

import android.content.Context;

import com.jianfanjia.cn.designer.bean.NotifyMessage;
import com.jianfanjia.cn.designer.dao.INotifyMessageDao;

import java.sql.SQLException;
import java.util.List;

/**
 * @author fengliang
 * @ClassName: NotifyMessageDao
 * @Description: TODO
 * @date 2015年9月27日 下午9:28:54
 */
public class NotifyMessageDao extends BaseDaoImpl<NotifyMessage> implements
        INotifyMessageDao {

    public NotifyMessageDao(Context context) {
        super(context, NotifyMessage.class);
    }

    @Override
    public List<NotifyMessage> getNotifyListByType(String type) {
        List<NotifyMessage> list = null;
        try {
            list = dao.queryBuilder().orderBy("time", false).where()
                    .eq("type", type).query();
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
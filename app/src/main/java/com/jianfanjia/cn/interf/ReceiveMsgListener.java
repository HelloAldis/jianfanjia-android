package com.jianfanjia.cn.interf;

import com.jianfanjia.cn.bean.NotifyMessage;

/**
 * Description:com.jianfanjia.cn.interf
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:16
 */
public interface ReceiveMsgListener {
    void onReceive(NotifyMessage message);
}
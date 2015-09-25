package com.jianfanjia.cn.interf;

import com.jianfanjia.cn.bean.NotifyMessage;

public interface ReceiveMsgListener {
	public boolean receive(NotifyMessage message);
}

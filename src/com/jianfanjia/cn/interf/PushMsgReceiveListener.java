package com.jianfanjia.cn.interf;

import com.jianfanjia.cn.bean.NotifyMessage;

public interface PushMsgReceiveListener {
	void onReceiveMsg(NotifyMessage message);
}

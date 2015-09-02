package com.jianfanjia.cn.interf;

import com.jianfanjia.cn.bean.Message;

public interface PushMsgReceiveListener {
	void onReceiveMsg(Message message);
}

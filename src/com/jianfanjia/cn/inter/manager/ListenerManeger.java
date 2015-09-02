package com.jianfanjia.cn.inter.manager;

import java.util.ArrayList;
import java.util.List;
import com.jianfanjia.cn.interf.PushMsgReceiveListener;

public class ListenerManeger {
	private static ListenerManeger listenerManeger;
	public static List<PushMsgReceiveListener> msgListeners = new ArrayList<PushMsgReceiveListener>();

	public static ListenerManeger getListenerManeger() {
		if (null == listenerManeger) {
			listenerManeger = new ListenerManeger();
		}
		return listenerManeger;
	}

	public void addPushMsgReceiveListener(PushMsgReceiveListener listener) {
		if (!msgListeners.contains(listener)) {
			msgListeners.add(listener);
		}
	}

	public void removePushMsgReceiveListener(PushMsgReceiveListener listener) {
		if (msgListeners.contains(listener)) {
			msgListeners.remove(listener);
		}
	}

}

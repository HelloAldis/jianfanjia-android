package com.jianfanjia.cn.inter.manager;

import java.util.ArrayList;
import java.util.List;
import android.util.Log;
import com.jianfanjia.cn.activity.NotifyActivity;
import com.jianfanjia.cn.bean.NotifyMessage;
import com.jianfanjia.cn.interf.ReceiveMsgListener;

public class ListenerManeger {
	private static final String TAG = ListenerManeger.class.getName();
	private static ListenerManeger listenerManeger;
	public static List<ReceiveMsgListener> msgListeners = new ArrayList<ReceiveMsgListener>();

	public static ListenerManeger getListenerManeger() {
		if (null == listenerManeger) {
			listenerManeger = new ListenerManeger();
		}
		return listenerManeger;
	}

	public void addReceiveMsgListener(ReceiveMsgListener listener) {
		if (!msgListeners.contains(listener)) {
			msgListeners.add(listener);
		}
	}

	public void removeReceiveMsgListener(ReceiveMsgListener listener) {
		if (msgListeners.contains(listener)) {
			msgListeners.remove(listener);
		}
	}

	public boolean receive(NotifyMessage message) {
		Log.i(TAG, "ListenerManeger msgListeners:" + msgListeners);
		for (ReceiveMsgListener listener : msgListeners) {
			if (listener instanceof NotifyActivity) {
				boolean result = listener.onReceive(message);
				Log.i(TAG, "ListenerManeger result£º" + result);
				return result;
			}
		}
		return false;
	}
}

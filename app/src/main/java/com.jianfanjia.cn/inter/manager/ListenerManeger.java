package com.jianfanjia.cn.inter.manager;

import android.util.Log;

import com.jianfanjia.cn.fragment.SiteManageFragment;
import com.jianfanjia.cn.interf.ReceiveMsgListener;

import java.util.ArrayList;
import java.util.List;

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

    public ReceiveMsgListener getReceiveMsgListener() {
        Log.i(TAG, "ListenerManeger msgListeners:" + msgListeners);
        for (ReceiveMsgListener listener : msgListeners) {
            if (listener instanceof SiteManageFragment) {
                return listener;
            }
        }
        return null;
    }
}

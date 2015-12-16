package com.jianfanjia.cn.designer.inter.manager;

import android.util.Log;

import com.jianfanjia.cn.designer.fragment.SiteManageFragment;
import com.jianfanjia.cn.designer.interf.ReceiveMsgListener;

import java.util.ArrayList;
import java.util.List;

public class ListenerManeger {
    private static final String TAG = ListenerManeger.class.getName();
    public static List<ReceiveMsgListener> msgListeners = new ArrayList<ReceiveMsgListener>();
    private static ListenerManeger listenerManeger;

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

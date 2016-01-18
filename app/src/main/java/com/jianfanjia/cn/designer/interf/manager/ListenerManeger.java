package com.jianfanjia.cn.designer.interf.manager;

import android.util.Log;

import com.jianfanjia.cn.designer.activity.requirement.MyProcessDetailActivity;
import com.jianfanjia.cn.designer.interf.ReceiveMsgListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Name: ListenerManeger
 * User: fengliang
 * Date: 2015-10-12
 * Time: 11:50
 */
public class ListenerManeger {
    private static final String TAG = ListenerManeger.class.getName();
    private static ListenerManeger listenerManeger;
    private static List<ReceiveMsgListener> msgListeners = new ArrayList<ReceiveMsgListener>();

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
        Log.i(TAG, "msgListeners:" + msgListeners);
        for (ReceiveMsgListener listener : msgListeners) {
            if (listener instanceof MyProcessDetailActivity) {
                return listener;
            }
        }
        return null;
    }
}  

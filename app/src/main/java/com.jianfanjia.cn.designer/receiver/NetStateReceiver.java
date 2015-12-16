package com.jianfanjia.cn.designer.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.util.Log;

import com.jianfanjia.cn.designer.interf.NetStateListener;

/**
 * @author fengliang
 * @ClassName: NetStateReceiver
 * @Description:网络状态监听广播
 * @date 2015-9-14 上午11:33:29
 */
public class NetStateReceiver extends BroadcastReceiver {
    private static final String TAG = "NetStateReceiver";
    private NetStateListener listener;

    public NetStateReceiver(NetStateListener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "action:" + action);
        if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo Mobile = connectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo Wifi = connectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (Mobile.getState().equals(State.DISCONNECTED)
                    && Wifi.getState().equals(State.DISCONNECTED)) {
                listener.onDisConnect();
            } else {
                listener.onConnect();
            }
        }
    }

}

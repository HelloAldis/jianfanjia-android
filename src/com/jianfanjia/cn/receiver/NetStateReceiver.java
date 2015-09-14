package com.jianfanjia.cn.receiver;

import com.jianfanjia.cn.interf.NetStateListener;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.util.Log;

/**
 * 
 * @ClassName: NetStateReceiver
 * @Description:ÍøÂç×´Ì¬¼àÌý¹ã²¥
 * @author fengliang
 * @date 2015-9-14 ÉÏÎç11:33:29
 * 
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

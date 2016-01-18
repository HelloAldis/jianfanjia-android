package com.jianfanjia.cn.designer.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Name: NetTool
 * User: fengliang
 * Date: 2015-10-12
 * Time: 16:40
 */
public class NetTool {
    // 手机网络类型
    public static final int NETTYPE_WIFI = 0x01;
    public static final int NETTYPE_CMWAP = 0x02;
    public static final int NETTYPE_CMNET = 0x03;

    /**
     * 判断数据网络/WIFI是否连接
     *
     * @param mContext
     * @return
     */
    public static boolean isNetworkAvailable(Context mContext) {
        boolean netStatus = false;
        try {
            ConnectivityManager connectManager = (ConnectivityManager) mContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectManager
                    .getActiveNetworkInfo();
            if (activeNetworkInfo != null) {
                if (activeNetworkInfo.isAvailable()
                        && activeNetworkInfo.isConnected()) {
                    netStatus = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return netStatus;
    }


}  

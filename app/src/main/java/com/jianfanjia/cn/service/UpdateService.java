package com.jianfanjia.cn.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Description:版本更新服务
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 16:14
 */
public class UpdateService extends Service {


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}

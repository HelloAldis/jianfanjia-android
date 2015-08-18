package com.jianfanjia.cn.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.jianfanjia.cn.tools.LogTool;

/**
 * 
 * @ClassName: UpdateService
 * @Description: 版本更新服务
 * @author fengliang
 * @date 2015-8-18 下午1:33:18
 * 
 */
public class UpdateService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		LogTool.d(this.getClass().getName(), "onCreate()");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		LogTool.d(this.getClass().getName(), "onStartCommand()");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		LogTool.d(this.getClass().getName(), "onDestroy()");
	}

}

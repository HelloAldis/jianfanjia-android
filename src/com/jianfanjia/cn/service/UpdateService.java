package com.jianfanjia.cn.service;

import java.io.File;
import java.io.FileOutputStream;
import org.apache.http.Header;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.http.HttpRestClient;
import com.jianfanjia.cn.tools.LogTool;
import com.loopj.android.http.BinaryHttpResponseHandler;

/**
 * 
 * @ClassName: UpdateService
 * @Description: 版本更新服务
 * @author fengliang
 * @date 2015-8-18 下午1:33:18
 * 
 */
public class UpdateService extends Service {
	private NotificationManager nManager;
	private static final int NotificationID = 1;
	private NotificationCompat.Builder builder;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		LogTool.d(this.getClass().getName(), "onCreate()");
		nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		LogTool.d(this.getClass().getName(), "onStartCommand()");
		if (intent != null) {
			String download_url = intent.getStringExtra(Constant.DOWNLOAD_URL);
			if (download_url != null) {
				String fileName = download_url.substring(download_url
						.lastIndexOf("/"));
				download(download_url, Constant.APK_PATH, fileName);
			}
		}
		return super.onStartCommand(intent, flags, startId);
	}

	// 下载最新apk
	private void download(String url, final String filePath,
			final String fileName) {
		// 指定文件类型
		String[] allowedContentTypes = new String[] { "image/png",
				"image/jpeg", "application/octet-stream", "application/zip",
				"application/vnd.android.package-archive" };
		HttpRestClient.get(url, new BinaryHttpResponseHandler(
				allowedContentTypes) {

			@Override
			public void onStart() {
				LogTool.d(this.getClass().getName(), "onStart()");
				builder = new NotificationCompat.Builder(
						getApplicationContext());
				builder.setSmallIcon(R.drawable.icon_logo);
				builder.setTicker("正在下载新版本");
				builder.setContentTitle("简繁家");
				builder.setContentText("正在下载,请稍后...");
				builder.setNumber(0);
				builder.setAutoCancel(true);
				nManager.notify(NotificationID, builder.build());
			}

			@Override
			public void onProgress(long bytesWritten, long totalSize) {
				LogTool.d(this.getClass().getName(), "bytesWritten:"
						+ bytesWritten + "  totalSize:" + totalSize);
				builder.setProgress((int) totalSize, (int) bytesWritten, false);
				String process = (int) ((bytesWritten * 1.0 / totalSize) * 100)
						+ "%";
				LogTool.d(this.getClass().getName(), "process:" + process);
				builder.setContentInfo((int) ((bytesWritten / (float) totalSize) * 100)
						+ "%");
				nManager.notify(NotificationID, builder.build());
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				LogTool.d(this.getClass().getName(), "onSuccess()");
				File file = new File(filePath);
				if (!file.exists()) {
					file.mkdirs();
				}
				File apkFile = new File(file, fileName);
				try {
					FileOutputStream fos = new FileOutputStream(apkFile);
					fos.write(arg2);
					fos.flush();
					fos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				stopSelf();
				nManager.cancel(NotificationID);
				installApk(apkFile);
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				LogTool.d(this.getClass().getName(), "arg3:" + arg3);
				nManager.cancel(NotificationID);
			}

		});
	}

	// 安装APK文件
	private void installApk(File file) {
		Intent intent = new Intent();
		// 因为service不是运行在任务栈中的，所以在启动activity的时候要加上启动模式
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		startActivity(intent);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		LogTool.d(this.getClass().getName(), "onDestroy()");
	}

}

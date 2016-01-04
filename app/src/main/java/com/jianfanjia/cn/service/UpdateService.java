package com.jianfanjia.cn.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.http.coreprogress.listener.impl.UIProgressListener;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.tools.DownLoadManager;
import com.jianfanjia.cn.tools.LogTool;

import java.io.File;

/**
 * Description:版本更新服务
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 16:14
 */
public class UpdateService extends Service implements ApiUiUpdateListener {
    // 指定文件类型
    private static String[] allowedContentTypes = new String[]{"image/png",
            "image/jpeg", "application/octet-stream", "application/zip",
            "application/vnd.android.package-archive"};
    private DownLoadManager downLoadManager;
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
        downLoadManager = DownLoadManager.getInstance();
        nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogTool.d(this.getClass().getName(), "onStartCommand()");
        if (intent != null) {
            String download_url = intent.getStringExtra(Constant.DOWNLOAD_URL);
            if (download_url != null) {
                int startPos = download_url.lastIndexOf("/");
                if(startPos != -1){
                    String fileName = download_url.substring(startPos);
                    downLoadManager.download(download_url, Constant.APK_PATH, fileName, this, uiProgressListener);
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void preLoad() {
        builder = new NotificationCompat.Builder(
                getApplicationContext());
        builder.setSmallIcon(R.mipmap.icon_notify);
        builder.setTicker("正在下载新版本");
        builder.setContentTitle("简繁家");
        builder.setContentText("正在下载,请稍后...");
        builder.setNumber(0);
        builder.setAutoCancel(true);
        nManager.notify(NotificationID, builder.build());
    }

    @Override
    public void loadSuccess(Object data) {
        if (data != null) {
            LogTool.d(this.getClass().getName(), data.toString());
            File apkFile = new File(data.toString());
            stopSelf();
            nManager.cancel(NotificationID);
            installApk(apkFile);
        }
    }

    @Override
    public void loadFailture(String error_msg) {
        nManager.cancel(NotificationID);
        stopSelf();
    }

    private UIProgressListener uiProgressListener = new UIProgressListener() {
        @Override
        public void onUIProgress(final long bytesWritten, final long totalSize, boolean done) {
            LogTool.d(this.getClass().getName(), "bytesWritten:"
                    + bytesWritten + "  totalSize:" + totalSize);
            String process = (int) ((bytesWritten * 1.0 / totalSize) * 100)
                    + "%";
            LogTool.d(this.getClass().getName(), "process:" + process);
            builder.setProgress((int) totalSize, (int) bytesWritten, false);
            builder.setContentInfo((int) ((bytesWritten / (float) totalSize) * 100)
                    + "%");
            nManager.notify(NotificationID, builder.build());
        }

        @Override
        public void onUIStart(long currentBytes, long contentLength, boolean done) {
            super.onUIStart(currentBytes, contentLength, done);
            LogTool.d("uiProgressListener", "onUiStart");
        }

        @Override
        public void onUIFinish(long currentBytes, long contentLength, boolean done) {
            super.onUIFinish(currentBytes, contentLength, done);
            LogTool.d("uiProgressListener", "onUiFinish");
        }
    };

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

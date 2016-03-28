package com.jianfanjia.common.tool;

import android.util.Log;

import com.jianfanjia.common.base.application.BaseApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Description:log工具类
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 12:45
 */
public class LogTool {

    private static final String LOG_FILE_DIR = "/MyLog";
    private static final String LOG_PATH = FileUtil.getAppCache(BaseApplication.getInstance(), LOG_FILE_DIR);// log存放路径
    private static final String LOG_FILE = LOG_PATH + "/log.txt";//log文件

    public static void d(String tag, String msg) {
        Log.i(tag, msg);
        final File file = new File(LOG_FILE);
        if (!file.exists()) {
            File parent = file.getParentFile();
            if (!parent.exists())
                parent.mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(file, "rw");
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss:SSS");
                Date date = new Date(System.currentTimeMillis());
                String time = simpleDateFormat.format(date);
                long fileLength = randomAccessFile.length();
                randomAccessFile.seek(fileLength);
                randomAccessFile.writeBytes(time + "   " + tag + "     " + msg
                        + "\r\n");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (randomAccessFile != null) {
                try {
                    randomAccessFile.close();
                    randomAccessFile = null;
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    public static void delete() {
        final File file = new File(LOG_FILE);
        if (file.exists()) {
            file.delete();
        }
    }
}

package com.jianfanjia.cn.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.sql.Date;
import java.text.SimpleDateFormat;
import android.util.Log;
import com.jianfanjia.cn.config.Constant;

/**
 * 
 * @ClassName: LogTool
 * @Description: log工具类
 * @author fengliang
 * @date 2015-8-18 下午1:23:35
 * 
 */
public class LogTool {
	public static void d(String tag, String msg) {
		Log.i(tag, msg);
		final File file = new File(Constant.LOG_PATH);
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
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
						"hh:mm:ss:SSS");
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
		final File file = new File(Constant.LOG_PATH);
		if (file.exists()) {
			file.delete();
		}
	}

}

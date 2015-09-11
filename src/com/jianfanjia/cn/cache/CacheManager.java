package com.jianfanjia.cn.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import android.content.Context;
import android.util.Log;
import com.jianfanjia.cn.tools.NetTool;

public class CacheManager {
	private static final String TAG = "CacheManager";
	// wifi环境下缓存时间
	private static long wifi_cache_time = 5 * 60 * 1000;
	// 数据连接保存时间
	private static long other_cache_time = 60 * 60 * 1000;

	/**
	 * 保存对象
	 * 
	 * @param ser
	 * @param file
	 * @throws IOException
	 */
	public static boolean saveObject(Context context, Serializable ser,
			String file) {
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try {
			fos = context.openFileOutput(file, Context.MODE_PRIVATE);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(ser);
			oos.flush();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				oos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 读取对象
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static Serializable readObject(Context context, String file) {
		if (!isExistDataCache(context, file)) {
			return null;
		}
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try {
			fis = context.openFileInput(file);
			ois = new ObjectInputStream(fis);
			return (Serializable) ois.readObject();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			// 反序列话失败，删除缓存文件
			if (e instanceof InvalidClassException) {
				File data = context.getFileStreamPath(file);
				data.delete();
			}
		} finally {
			try {
				ois.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				fis.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 缓存是否存在
	 * 
	 * @param cachefile
	 * @return
	 */
	public static boolean isExistDataCache(Context context, String cachefile) {
		if (context == null)
			return false;
		boolean exist = false;
		File data = context.getFileStreamPath(cachefile);
		if (data.exists()) {
			exist = true;
		}
		return exist;
	}

	/**
	 * 缓存是否已经过期
	 */
	public static boolean isCacheDataFailure(Context context, String cachefile) {
		File data = context.getFileStreamPath(cachefile);
		if (!data.exists()) {
			Log.i(TAG, "文件不存在");
			return false;
		}
		boolean failure = false;
		long existTime = System.currentTimeMillis() - data.lastModified();
		if (NetTool.getNetworkType() == NetTool.NETTYPE_WIFI) {
			failure = existTime > wifi_cache_time ? false : true;
		} else if (NetTool.getNetworkType() == NetTool.NETTYPE_CMNET
				|| NetTool.getNetworkType() == NetTool.NETTYPE_CMWAP) {
			failure = existTime > other_cache_time ? false : true;
		}
		return failure;
	}

	/**
	 * 
	 * @param context
	 * @param cacheFile
	 * @return
	 */
	public static Object getObjectByFile(Context context, String cacheFile) {
		Object object = null;
		if (NetTool.isNetworkAvailable(context)) {
			if (CacheManager.isCacheDataFailure(context, cacheFile)) {
				Log.i(TAG, "缓存有效");
				object = CacheManager.readObject(context, cacheFile);
				return object;
			} else {
				Log.i(TAG, "缓存无效");
				return null;
			}
		} else {
			object = CacheManager.readObject(context, cacheFile);
			return object;
		}
	}
}

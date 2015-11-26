package com.jianfanjia.cn.application;

import android.content.Context;
import android.content.pm.PackageManager;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.BaseApplication;
import com.jianfanjia.cn.bean.ProcessInfo;
import com.jianfanjia.cn.bean.RegisterInfo;
import com.jianfanjia.cn.cache.DataCleanManager;
import com.jianfanjia.cn.cache.DataManagerNew;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.http.OkHttpClientManager;
import com.jianfanjia.cn.http.cookie.PersistentCookieStore;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.okhttp.OkHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.CookieManager;
import java.net.CookiePolicy;

/**
 * @version 1.0
 * @Description 此类是我的应用程序类
 * @author Administrator
 * @date 2015-8-20 下午1:45
 * 
 */
public class MyApplication extends BaseApplication {
	private static MyApplication instance;
	private RegisterInfo registerInfo = new RegisterInfo();// 注册实体信息
	private PersistentCookieStore cookieStore;// cookie实例化

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;

		cookieStore = new PersistentCookieStore(this);// 记录cookie
		saveCookie(OkHttpClientManager.getInstance().client());
		/*
		 * Thread.setDefaultUncaughtExceptionHandler(AppException
		 * .getAppExceptionHandler(this));
		 */
	}

	public void saveDefaultProcess() {
		ProcessInfo processInfo = DataManagerNew.getInstance()
				.getProcessInfoById(Constant.DEFAULT_PROCESSINFO_ID);
		if (processInfo == null) {
			processInfo = getDefaultProcessInfo(this);
			DataManagerNew.getInstance().saveProcessInfo(processInfo);
			LogTool.d(this.getClass().getName(),
					"default_processIndo save success");
		}
	}

	// 拿到所有的模拟工地数据
	public static ProcessInfo getDefaultProcessInfo(Context context) {
		ProcessInfo processInfo = null;
		try {
			InputStream is = context.getAssets()
					.open("default_processinfo.txt");
			String jsonString = StringUtils.toConvertString(is);
			processInfo = JsonParser.jsonToBean(jsonString, ProcessInfo.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return processInfo;
	}

	public static MyApplication getInstance() {
		return instance;
	}

	public RegisterInfo getRegisterInfo() {
		return registerInfo;
	}

	/**
	 * @description 根据英文的name,拿到中文的name
	 * @param name
	 * @return
	 */
	public String getStringById(String name) {
		int StringId = getResources().getIdentifier(name, "string",
				getPackageName());
		return getResources().getString(StringId);
	}

	/**
	 * @description 根据name,拿到当前的进行工序的位置
	 * @param name
	 * @return 默认返回0，当前工序为第一个工序
	 */
	public int getPositionByItemName(String name) {
		if (name == null) {
			return 0;
		}
		String[] items = getResources().getStringArray(R.array.site_data);
		for (int i = 0; i < items.length; i++) {
			if (items[i].equals(name)) {
				return i;
			}
		}
		return 0;
	}

	/**
	 * 拿到版本号
	 * 
	 * @return
	 */
	public int getVersionCode() {
		int versionCode = 0;
		try {
			versionCode = getPackageManager().getPackageInfo(getPackageName(),
					0).versionCode;
		} catch (PackageManager.NameNotFoundException ex) {
			versionCode = 0;
		}
		return versionCode;
	}

	public String getVersionName() {
		String name = "";
		try {
			name = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
		} catch (PackageManager.NameNotFoundException ex) {
			name = "";
		}
		return name;
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 判断当前版本是否兼容目标版本的方法
	 * 
	 * @param VersionCode
	 * @return
	 */
	public static boolean isMethodsCompat(int VersionCode) {
		int currentVersion = android.os.Build.VERSION.SDK_INT;
		return currentVersion >= VersionCode;
	}

	/**
	 * 清除app缓存
	 */
	public void clearAppCache() {
//		DataCleanManager.cleanDatabases(this);
		// 清除数据缓存
//		DataCleanManager.cleanInternalCache(this);
		ImageLoader.getInstance().clearDiskCache();
//		DataCleanManager.deleteFilesByDirectory(new File(Constant.COMMON_PATH));
		
		//
		// 2.2版本才有将应用缓存转移到sd卡的功能
		if (isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)) {
			DataCleanManager.cleanCustomCache(getExternalCacheDir());
		}
	}

	public void saveCookie(OkHttpClient client) {
		client.setCookieHandler(new CookieManager(cookieStore, CookiePolicy.ACCEPT_ORIGINAL_SERVER));
	}


	public void clearCookie() {
		cookieStore.removeAll();
	}
}

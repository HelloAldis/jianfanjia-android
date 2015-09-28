package com.jianfanjia.cn.application;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.http.cookie.Cookie;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.BaseApplication;
import com.jianfanjia.cn.bean.ProcessInfo;
import com.jianfanjia.cn.bean.RegisterInfo;
import com.jianfanjia.cn.cache.DataCleanManager;
import com.jianfanjia.cn.cache.DataManagerNew;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.http.HttpRestClient;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.StringUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;

/**
 * @version 1.0
 * @Description �������ҵ�Ӧ�ó�����
 * @author Administrator
 * @date 2015-8-20 ����1:45
 * 
 */
public class MyApplication extends BaseApplication {
	private static MyApplication instance;
	private RegisterInfo registerInfo = new RegisterInfo();// ע��ʵ����Ϣ
	private PersistentCookieStore cookieStore;// cookieʵ����

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		
		saveDefaultProcess();//����Ĭ�ϵĹ�����Ϣ

		cookieStore = new PersistentCookieStore(this);// ��¼cookie
		saveCookie(HttpRestClient.getHttpClient());
		/*
		 * Thread.setDefaultUncaughtExceptionHandler(AppException
		 * .getAppExceptionHandler(this));
		 */
	}

	public void saveDefaultProcess() {
		ProcessInfo processInfo = DataManagerNew.getInstance().getProcessInfoById(Constant.DEFAULT_PROCESSINFO_ID);
		if(processInfo == null){
			processInfo = getDefaultProcessInfo(this);
			DataManagerNew.getInstance().saveProcessInfo(processInfo);
			LogTool.d(this.getClass().getName(), "default_processIndo save success");
		}
	}

	// �õ����е�ģ�⹤������
	public static ProcessInfo getDefaultProcessInfo(Context context) {
		ProcessInfo processInfo = null;
		try {
			InputStream is = context.getAssets().open("default_processinfo.txt");
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
	 * @description ����Ӣ�ĵ�name,�õ����ĵ�name
	 * @param string
	 * @return
	 */
	public String getStringById(String name) {
		int StringId = getResources().getIdentifier(name, "string",
				getPackageName());
		return getResources().getString(StringId);
	}

	/**
	 * @description ����name,�õ���ǰ�Ľ��й����λ��
	 * @param name
	 * @return Ĭ�Ϸ���0����ǰ����Ϊ��һ������
	 */
	public int getPositionByItemName(String name) {
		if(name == null){
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
	 * �õ��汾��
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
	 * �жϵ�ǰ�汾�Ƿ����Ŀ��汾�ķ���
	 * 
	 * @param VersionCode
	 * @return
	 */
	public static boolean isMethodsCompat(int VersionCode) {
		int currentVersion = android.os.Build.VERSION.SDK_INT;
		return currentVersion >= VersionCode;
	}

	/**
	 * ���app����
	 */
	public void clearAppCache() {
		DataCleanManager.cleanDatabases(this);
		// ������ݻ���
		DataCleanManager.cleanInternalCache(this);
		//
		// 2.2�汾���н�Ӧ�û���ת�Ƶ�sd���Ĺ���
		if (isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)) {
			DataCleanManager.cleanCustomCache(getExternalCacheDir());
		}
	}

	public void saveCookie(AsyncHttpClient client) {
		client.setCookieStore(cookieStore);
		for (Cookie cookie : cookieStore.getCookies()) {
			Log.i("COOKIE", cookie.getName() + " " + cookie.getValue());
		}
	}

	public void clearCookie() {
		cookieStore.clear();
	}
}

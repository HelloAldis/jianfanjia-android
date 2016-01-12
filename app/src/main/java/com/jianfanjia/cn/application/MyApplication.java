package com.jianfanjia.cn.application;

import android.content.Context;
import android.content.pm.PackageManager;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.BaseApplication;
import com.jianfanjia.cn.cache.DataCleanManager;
import com.jianfanjia.cn.http.OkHttpClientManager;
import com.jianfanjia.cn.http.cookie.PersistentCookieStore;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.okhttp.OkHttpClient;
import com.umeng.socialize.PlatformConfig;

import java.net.CookieManager;
import java.net.CookiePolicy;

/**
 * Description:此类是我的应用程序类
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 16:19
 */
public class MyApplication extends BaseApplication {
    private static MyApplication instance;
    private PersistentCookieStore cookieStore;// cookie实例化

//    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
//        saveDefaultProcess();// 加载默认的工地信息
        cookieStore = new PersistentCookieStore(this);// 记录cookie
        saveCookie(OkHttpClientManager.getInstance().client());

        PlatformConfig.setWeixin("wx391daabfce27e728", "f7c8e3e1b5910dd93be2744dacb3a1cc");
        PlatformConfig.setSinaWeibo("10611350", "4a5b93b71687ec9af1ee91cfdfb361d3");
        PlatformConfig.setQQZone("1104973048", "FuDs7s4vJGAEzCrz");


        /*
         * Thread.setDefaultUncaughtExceptionHandler(AppException
		 * .getAppExceptionHandler(this));
		 */
//        refWatcher = LeakCanary.install(this);
    }

    /*public static RefWatcher getRefWatcher(Context context) {
        MyApplication application = (MyApplication) context.getApplicationContext();
        return application.refWatcher;
    }
*/

    public static MyApplication getInstance() {
        return instance;
    }

    /**
     * @param name
     * @return
     * @description 根据英文的name, 拿到中文的name
     */
    public String getStringById(String name) {
        int StringId = getResources().getIdentifier(name, "string",
                getPackageName());
        return getResources().getString(StringId);
    }

    /**
     * @param name
     * @return 默认返回0，当前工序为第一个工序
     * @description 根据name, 拿到当前的进行工序的位置
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
		DataCleanManager.cleanDatabases(this);
        // 清除数据缓存
		DataCleanManager.cleanInternalCache(this);
        ImageLoader.getInstance().clearDiskCache();
        ImageLoader.getInstance().clearMemoryCache();
        //
        // 2.2版本才有将应用缓存转移到sd卡的功能
        if (isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)) {
            DataCleanManager.cleanCustomCache(getExternalCacheDir());
        }
    }

    public void saveCookie(OkHttpClient client) {
        client.setCookieHandler(new CookieManager(cookieStore, CookiePolicy.ACCEPT_ALL));

    }


    public void clearCookie() {
        cookieStore.removeAll();
    }
}

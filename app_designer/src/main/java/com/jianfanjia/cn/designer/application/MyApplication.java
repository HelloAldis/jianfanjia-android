package com.jianfanjia.cn.designer.application;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.StrictMode;

import java.net.CookieStore;

import com.jianfanjia.api.ApiClient;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.api.BaseApiCallbackImpl;
import com.jianfanjia.common.tool.DataCleanTool;
import com.jianfanjia.cn.designer.config.Constant;
import com.jianfanjia.cn.designer.http.cookie.PersistentCookieStore;
import com.jianfanjia.common.base.application.BaseApplication;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * Description:此类是我的应用程序类
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 16:19
 */
public class MyApplication extends BaseApplication {

//    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();

        if (Constant.Config.DEVELOPER_MODE
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll().penaltyDialog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectAll().penaltyDeath().build());
        }

        initImageLoader(this);
        initApiClient();

//        refWatcher = LeakCanary.install(this);
    }

    private void initApiClient() {
        CookieStore store = new PersistentCookieStore(BaseApplication.getInstance().getApplicationContext());
        ApiClient.init(store, new BaseApiCallbackImpl());
    }

    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you
        // may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(
                context);
        config.memoryCacheExtraOptions(480, 800);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.threadPoolSize(3);
        config.memoryCache(new WeakMemoryCache());
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.memoryCacheSize(2 * 1024 * 1024);
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

    /*public static RefWatcher getRefWatcher(Context context) {
        MyApplication application = (MyApplication) context.getApplicationContext();
        return application.refWatcher;
    }
*/

    public static MyApplication getInstance() {
        return (MyApplication) baseApplication;
    }

    /**
     * @param name
     * @return
     * @description 根据英文的name, 拿到中文的name
     */
    public String getStringById(String name) {
        String str = null;
        try {
            int StringId = getResources().getIdentifier(name, "string",
                    getPackageName());
            str = getResources().getString(StringId);
            return str;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
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
        DataCleanTool.cleanDatabases(this);
        // 清除数据缓存
        DataCleanTool.cleanInternalCache(this);
        ImageLoader.getInstance().clearDiskCache();
        ImageLoader.getInstance().clearMemoryCache();
        //
        // 2.2版本才有将应用缓存转移到sd卡的功能
        if (isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)) {
            DataCleanTool.cleanCustomCache(getExternalCacheDir());
        }
    }
}

package com.jianfanjia.cn.tools;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * 
 * @ClassName: VersionTool
 * @Description: 版本工具类
 * @author fengliang
 * @date 2015-9-2 上午10:26:07
 * 
 */
public class VersionTool {
	public static String getVersionCode(Context context) {
		// 获取PackageManager 实例
		PackageManager packageManager = context.getPackageManager();
		// 获得context所属类的包名，0表示获取版本信息
		PackageInfo packageInfo;
		try {
			packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			return packageInfo.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}

package com.jianfanjia.cn.tools;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * 
 * @ClassName: VersionTool
 * @Description: �汾������
 * @author fengliang
 * @date 2015-9-2 ����10:26:07
 * 
 */
public class VersionTool {
	public static String getVersionCode(Context context) {
		// ��ȡPackageManager ʵ��
		PackageManager packageManager = context.getPackageManager();
		// ���context������İ�����0��ʾ��ȡ�汾��Ϣ
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

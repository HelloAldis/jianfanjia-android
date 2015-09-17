package com.jianfanjia.cn.tools;

import java.util.LinkedList;
import java.util.List;
import android.app.Activity;

/**
 * 
 * @ClassName: ActivityManager
 * @Description: �˳�����Activity��ͨ���࣬ActivityManager
 * @author fengliang
 * @date 2015-9-17 ����12:18:56
 * 
 */
public class ActivityManager {
	private List<Activity> activityList = new LinkedList<Activity>();
	private static ActivityManager instance;

	private ActivityManager() {

	}

	// ����ģʽ�л�ȡΨһ��MyApplicationʵ��
	public static ActivityManager getInstance() {
		if (null == instance) {
			instance = new ActivityManager();
		}
		return instance;
	}

	// ���Activity��������
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	// ��������Activity��finish
	public void exit() {
		for (Activity activity : activityList) {
			activity.finish();
		}
		System.exit(0);
	}
}

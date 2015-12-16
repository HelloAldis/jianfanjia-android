package com.jianfanjia.cn.designer.tools;

import android.app.Activity;

import java.util.LinkedList;
import java.util.List;

/**
 * @author fengliang
 * @ClassName: ActivityManager
 * @Description: 退出所有Activity的通用类，ActivityManager
 * @date 2015-9-17 下午12:18:56
 */
public class ActivityManager {
    private static ActivityManager instance;
    private List<Activity> activityList = new LinkedList<Activity>();

    private ActivityManager() {

    }

    // 单例模式中获取唯一的MyApplication实例
    public static ActivityManager getInstance() {
        if (null == instance) {
            instance = new ActivityManager();
        }
        return instance;
    }

    // 添加Activity到容器中
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    // 遍历所有Activity并finish
    public void exit() {
        for (Activity activity : activityList) {
            activity.finish();
        }
    }
}

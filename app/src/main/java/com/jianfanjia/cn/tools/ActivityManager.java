package com.jianfanjia.cn.tools;

import android.app.Activity;
import java.util.LinkedList;
import java.util.List;

/**
 * Description:退出所有Activity的通用类，ActivityManager
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 12:43
 */
public class ActivityManager {
    private List<Activity> activityList = new LinkedList<Activity>();
    private static ActivityManager instance;

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

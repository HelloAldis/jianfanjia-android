package com.jianfanjia.cn;

import android.app.Activity;

import com.jianfanjia.cn.tools.LogTool;

import java.util.Iterator;
import java.util.Stack;

/**
 * activity堆栈式管理
 * 
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @created 2014年10月30日 下午6:22:05
 * 
 */
public class AppManager {

    private static Stack<Activity> activityStack = new Stack<Activity>();
    private static AppManager instance;

    private AppManager() {}

    /**
     * 单一实例
     */
    public static AppManager getAppManager() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
        LogTool.d(this.getClass().getName(), currentActivity().getClass().getName());
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        if(!activityStack.empty()){
            Activity activity = activityStack.peek();
            return activity;
        }
        return null;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        LogTool.d(this.getClass().getName(), activity.getClass().getName() + " finish()");
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
                break;
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        LogTool.d(this.getClass().getName(), "activityStack.size() =" + activityStack.size());
        Iterator<Activity> iterator = activityStack.iterator();
        while (iterator.hasNext()){
            Activity activity = iterator.next();
            LogTool.d(this.getClass().getName(), "activityStack.name() =" + activity.getClass().getName());
            activity.finish();
            activity = null;
        }
        activityStack.clear();
    }

    /**
     * 获取指定的Activity
     * 
     * @author kymjs
     */
    public static Activity getActivity(Class<?> cls) {
        if (activityStack != null)
            for (Activity activity : activityStack) {
                if (activity.getClass().equals(cls)) {
                    return activity;
                }
            }
        return null;
    }

    /**
     * 退出应用程序
     */
    public void AppExit() {
        try {
            finishAllActivity();
            // 杀死该应用进程
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        } catch (Exception e) {
        }
    }
}
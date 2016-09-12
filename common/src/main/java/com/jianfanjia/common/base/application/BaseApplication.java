package com.jianfanjia.common.base.application;

import android.app.Application;

/**
 * Created by jyz on 16/3/25.
 */
public class BaseApplication extends Application {

    protected static BaseApplication baseApplication = null;

    @Override
    public void onCreate() {
        super.onCreate();

        BaseApplication.baseApplication = this;
    }

    public static BaseApplication getInstance() {
        return baseApplication;
    }
}

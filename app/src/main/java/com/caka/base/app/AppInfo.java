package com.caka.base.app;

import android.support.multidex.MultiDexApplication;

import com.caka.base.utils.AppUtils;

public class AppInfo extends MultiDexApplication {

    private static volatile AppInfo instance;

    public static AppInfo getInstance() {
        return instance;
    }

    public static void setInstance(AppInfo instance) {
        AppInfo.instance = instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setInstance(this);
        new AppUtils(this);
    }

}

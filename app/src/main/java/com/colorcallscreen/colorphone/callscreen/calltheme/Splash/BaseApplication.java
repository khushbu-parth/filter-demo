package com.colorcallscreen.colorphone.callscreen.calltheme.Splash;

import android.content.Context;

import com.ads.control.application.AdsMultiDexApplication;

public class BaseApplication extends AdsMultiDexApplication {
    public static Context context;
    private static BaseApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        context = this;

    }

    public static Context getContext() {
        return context;
    }

    public static BaseApplication getApp() {
        return instance;
    }
}

package com.co.casttotv.screenmirroring.mirroring.cast;

import android.app.Application;

import com.ads.sdk.SdkManager;
import com.co.casttotv.screenmirroring.mirroring.cast.activities.SplashActivity;
import com.co.casttotv.screenmirroring.mirroring.cast.prefrences.AppPref;
import com.connectsdk.discovery.DiscoveryManager;

public class MyApplication extends Application {

    public static MyApplication myApplication;

    public static MyApplication getInstance() {
        return myApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        DiscoveryManager.init(this);
        AppPref.init(this);

        SdkManager.initialize(this, SplashActivity.class);
    }
}

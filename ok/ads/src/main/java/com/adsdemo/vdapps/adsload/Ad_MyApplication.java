package com.adsdemo.vdapps.adsload;

import android.app.Application;

import com.adsdemo.vdapps.adsload.AppOpenManager;
import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;


public class Ad_MyApplication extends Application {

    public AppOpenManager appOpenManager;

    @Override
    public void onCreate() {
        super.onCreate();

        AudienceNetworkAds.initialize(this);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });

        appOpenManager = new AppOpenManager(this);

    }
}
